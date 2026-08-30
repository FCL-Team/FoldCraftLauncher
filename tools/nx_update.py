#!/usr/bin/env python3
"""Pull FCL-Team's upstream changes into the nx-launcher branch and trigger a CI build.

Usage: python3 tools/nx_update.py

Requires: git remotes 'origin' (the exgg1453/NX-Launcher fork) and
'upstream' (FCL-Team/FoldCraftLauncher) already configured, and optionally
the GitHub CLI ('gh', authenticated) to report the triggered build's status.
"""

import json
import subprocess
import sys
import time
from pathlib import Path

sys.stdout.reconfigure(line_buffering=True)  # keep progress visible when piped, not just on a TTY

WORK_BRANCH = "nx-launcher"
POLL_INTERVAL_SECONDS = 15
POLL_TIMEOUT_SECONDS = 15 * 60


def run(args, check=True, capture=True):
    result = subprocess.run(
        args, cwd=REPO_ROOT, text=True,
        stdout=subprocess.PIPE if capture else None,
        stderr=subprocess.STDOUT if capture else None,
    )
    if check and result.returncode != 0:
        if capture:
            print(result.stdout)
        sys.exit(f"command failed: {' '.join(args)}")
    return (result.stdout or "").strip()


def has_gh():
    return subprocess.run(["gh", "--version"], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL).returncode == 0


def remote_default_branch(remote):
    output = run(["git", "remote", "show", remote])
    for line in output.splitlines():
        line = line.strip()
        if line.startswith("HEAD branch:"):
            return line.split(":", 1)[1].strip()
    sys.exit(f"could not determine default branch for remote '{remote}'")


def main():
    remotes = run(["git", "remote"]).splitlines()
    for required in ("origin", "upstream"):
        if required not in remotes:
            sys.exit(f"missing git remote '{required}' - run this from the nx-launcher fork checkout")

    if run(["git", "status", "--porcelain"]):
        sys.exit("working tree has uncommitted changes, commit or stash them first")

    print("Fetching upstream (FCL-Team) and origin (fork)...")
    run(["git", "fetch", "upstream"], capture=False)
    run(["git", "fetch", "origin"], capture=False)

    upstream_branch = remote_default_branch("upstream")
    print(f"Upstream default branch: {upstream_branch}")

    run(["git", "checkout", WORK_BRANCH])

    merge = subprocess.run(
        ["git", "merge", f"upstream/{upstream_branch}", "--no-edit"],
        cwd=REPO_ROOT, text=True, capture_output=True,
    )
    if merge.returncode != 0:
        print(merge.stdout)
        print(merge.stderr)
        conflicts = run(["git", "diff", "--name-only", "--diff-filter=U"], check=False)
        print("\nMerge stopped with conflicts, left unresolved for manual fixing:")
        print(conflicts or "(see git status)")
        sys.exit(1)

    print(f"Merged upstream/{upstream_branch} into {WORK_BRANCH} cleanly.")

    print(f"Pushing {WORK_BRANCH} to origin...")
    run(["git", "push", "origin", WORK_BRANCH], capture=False)

    if not has_gh():
        print("\n'gh' CLI not available - skipping build status check. "
              "Check the Actions tab on your fork manually.")
        return

    commit = run(["git", "rev-parse", "HEAD"])
    print(f"\nWaiting for the Actions build for commit {commit[:8]}...")
    deadline = time.time() + POLL_TIMEOUT_SECONDS
    run_id = None
    while time.time() < deadline:
        out = run([
            "gh", "run", "list",
            "--branch", WORK_BRANCH,
            "--limit", "5",
            "--json", "databaseId,headSha,status,conclusion,url",
        ])
        for row in json.loads(out or "[]"):
            if row["headSha"] == commit:
                run_id = row["databaseId"]
                if row["status"] == "completed":
                    conclusion = row["conclusion"]
                    print(f"Build {conclusion}: {row['url']}")
                    if conclusion == "success":
                        run(["gh", "run", "view", str(run_id)], capture=False)
                    return
        time.sleep(POLL_INTERVAL_SECONDS)

    if run_id:
        print(f"Timed out waiting for the build to finish, check it manually: "
              f"gh run view {run_id}")
    else:
        print("Timed out before the build even started - check the Actions tab manually.")


if __name__ == "__main__":
    REPO_ROOT = Path(
        subprocess.run(
            ["git", "rev-parse", "--show-toplevel"], text=True, capture_output=True, check=True
        ).stdout.strip()
    )
    main()
