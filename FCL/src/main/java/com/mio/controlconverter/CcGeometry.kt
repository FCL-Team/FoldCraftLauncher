package com.mio.controlconverter

import com.google.gson.JsonObject

/**
 * 几何与启发式推断（对应 cc.py/geometry.rs：按钮矩形、重叠合并评分、
 * 方向网格签名、 reciprocal opener 推断、内置菜单词表）。
 */
object CcGeometry {

    data class Rect(val x1: Double, val y1: Double, val x2: Double, val y2: Double)

    fun fclButtonRect(button: JsonObject, aspect: Double): Rect {
        val baseInfo = button.optObj("baseInfo") ?: JsonObject()
        val screenH = 10000.0
        val screenW = screenH * Math.max(0.1, CcUtils.clampFloat(CcJson.pyNum(aspect), 16.0 / 9.0))

        val width: Double
        val height: Double
        if (CcJson.toStringV(baseInfo.opt("sizeType")) == "ABSOLUTE") {
            width = Math.max(1.0, CcUtils.clampZlDp(baseInfo.opt("absoluteWidth"), 50.0) * 10.0)
            height = Math.max(1.0, CcUtils.clampZlDp(baseInfo.opt("absoluteHeight"), 50.0) * 10.0)
        } else {
            val pw = baseInfo.optObj("percentageWidth") ?: JsonObject()
            val ph = baseInfo.optObj("percentageHeight") ?: JsonObject()
            val widthRef = if (CcJson.toStringV(pw.opt("reference")) == "SCREEN_HEIGHT") screenH else screenW
            val heightRef = if (CcJson.toStringV(ph.opt("reference")) == "SCREEN_HEIGHT") screenH else screenW
            width = Math.max(1.0, widthRef * CcUtils.clampInt(pw.opt("size"), 50) / 1000.0)
            height = Math.max(1.0, heightRef * CcUtils.clampInt(ph.opt("size"), 50) / 1000.0)
        }
        val x = (screenW - width) * CcUtils.clampInt(baseInfo.opt("xPosition"), 0) / 1000.0
        val y = (screenH - height) * CcUtils.clampInt(baseInfo.opt("yPosition"), 0) / 1000.0
        return Rect(x, y, x + width, y + height)
    }

    fun rectArea(r: Rect): Double = Math.max(0.0, r.x2 - r.x1) * Math.max(0.0, r.y2 - r.y1)

    fun screenArea(aspect: Double): Double =
        10000.0 * 10000.0 * Math.max(0.1, CcUtils.clampFloat(CcJson.pyNum(aspect), 16.0 / 9.0))

    fun fclButtonAreaRatio(button: JsonObject, aspect: Double): Double =
        rectArea(fclButtonRect(button, aspect)) / Math.max(1.0, screenArea(aspect))

    private fun rectOverlapArea(a: Rect, b: Rect): Double =
        Math.max(0.0, Math.min(a.x2, b.x2) - Math.max(a.x1, b.x1)) *
            Math.max(0.0, Math.min(a.y2, b.y2) - Math.max(a.y1, b.y1))

    private fun rectContainsPoint(r: Rect, px: Double, py: Double): Boolean =
        r.x1 <= px && px <= r.x2 && r.y1 <= py && py <= r.y2

    private fun rectGap(a: Rect, b: Rect): Pair<Double, Double> {
        val horizontal = Math.max(0.0, Math.max(a.x1, b.x1) - Math.min(a.x2, b.x2))
        val vertical = Math.max(0.0, Math.max(a.y1, b.y1) - Math.min(a.y2, b.y2))
        return Pair(horizontal, vertical)
    }

    private fun rectDistance(a: Rect, b: Rect): Double {
        val (horizontal, vertical) = rectGap(a, b)
        return kotlin.math.hypot(horizontal, vertical)
    }

    fun sameVisibility(a: JsonObject, b: JsonObject): Boolean {
        val aVis = CcJson.toStringV((a.optObj("baseInfo") ?: JsonObject()).opt("visibilityType")).ifEmpty { "ALWAYS" }
        val bVis = CcJson.toStringV((b.optObj("baseInfo") ?: JsonObject()).opt("visibilityType")).ifEmpty { "ALWAYS" }
        return aVis == bVis
    }

    private fun overlayMatchScore(eventButton: JsonObject, displayButton: JsonObject, aspect: Double): Double {
        val eventRect = fclButtonRect(eventButton, aspect)
        val displayRect = fclButtonRect(displayButton, aspect)
        val eventArea = rectArea(eventRect)
        val displayArea = rectArea(displayRect)
        if (eventArea <= 0.0 || displayArea <= 0.0) return 0.0

        val overlap = rectOverlapArea(eventRect, displayRect)
        val overlapMin = overlap / Math.max(1.0, Math.min(eventArea, displayArea))
        val displayCenterInEvent = rectContainsPoint(
            eventRect, (displayRect.x1 + displayRect.x2) / 2.0, (displayRect.y1 + displayRect.y2) / 2.0
        )
        val eventCenterInDisplay = rectContainsPoint(
            displayRect, (eventRect.x1 + eventRect.x2) / 2.0, (eventRect.y1 + eventRect.y2) / 2.0
        )
        val (horizontalGap, verticalGap) = rectGap(eventRect, displayRect)
        val eventW = eventRect.x2 - eventRect.x1
        val eventH = eventRect.y2 - eventRect.y1
        val displayW = displayRect.x2 - displayRect.x1
        val displayH = displayRect.y2 - displayRect.y1
        val verticalOverlap = Math.max(0.0, Math.min(eventRect.y2, displayRect.y2) - Math.max(eventRect.y1, displayRect.y1)) /
            Math.max(1.0, Math.min(eventH, displayH))
        val horizontalOverlap = Math.max(0.0, Math.min(eventRect.x2, displayRect.x2) - Math.max(eventRect.x1, displayRect.x1)) /
            Math.max(1.0, Math.min(eventW, displayW))

        if (overlapMin >= 0.25 || displayCenterInEvent || eventCenterInDisplay) {
            var score = 100.0 + overlapMin * 100.0
            if (displayCenterInEvent) score += 25.0
            if (eventCenterInDisplay) score += 10.0
            return score
        }
        val maxW = Math.max(eventW, displayW)
        val maxH = Math.max(eventH, displayH)
        if (verticalOverlap >= 0.65 && horizontalGap <= Math.max(250.0, maxW * 0.25)) {
            return 40.0 + verticalOverlap * 20.0 - horizontalGap / Math.max(1.0, maxW)
        }
        if (horizontalOverlap >= 0.65 && verticalGap <= Math.max(250.0, maxH * 0.25)) {
            return 40.0 + horizontalOverlap * 20.0 - verticalGap / Math.max(1.0, maxH)
        }
        return 0.0
    }

    /**
     * FCL「显示/事件分离按钮」配对：返回 Pair(事件按钮下标 -> 显示按钮下标, 被消费的显示按钮下标)。
     */
    fun matchFclOverlayButtons(
        buttons: List<JsonObject>,
        aspect: Double,
    ): Pair<MutableMap<Int, Int>, MutableSet<Int>> {
        val displayIndices = mutableListOf<Int>()
        val eventIndices = mutableListOf<Int>()
        for ((i, button) in buttons.withIndex()) {
            val text = CcJson.toStringV(button.opt("text"))
            if (!CcEvents.fclButtonHasPayload(button) && text.trim().isNotEmpty()) displayIndices.add(i)
            if (CcEvents.fclButtonHasPayload(button) && text.trim().isEmpty()) eventIndices.add(i)
        }
        val matches = linkedMapOf<Int, Int>()
        val consumed = linkedSetOf<Int>()

        for (eventIndex in eventIndices) {
            val eventButton = buttons[eventIndex]
            var bestIndex = -1
            var bestScore = 0.0
            for (displayIndex in displayIndices) {
                if (displayIndex in consumed) continue
                val displayButton = buttons[displayIndex]
                if (!sameVisibility(eventButton, displayButton)) continue
                val score = overlayMatchScore(eventButton, displayButton, aspect)
                if (score > bestScore) {
                    bestScore = score
                    bestIndex = displayIndex
                }
            }
            if (bestIndex >= 0 && bestScore >= 40.0) {
                matches[eventIndex] = bestIndex
                consumed.add(bestIndex)
            }
        }
        return Pair(matches, consumed)
    }

    private data class GridSignature(val style: String, val width: Long, val height: Long, val visibility: String)

    private fun fclButtonGridSignature(button: JsonObject): GridSignature {
        val baseInfo = button.optObj("baseInfo") ?: JsonObject()
        val pw = baseInfo.optObj("percentageWidth") ?: JsonObject()
        val ph = baseInfo.optObj("percentageHeight") ?: JsonObject()
        return GridSignature(
            style = CcJson.toStringV(button.opt("style")),
            width = CcUtils.clampInt(pw.opt("size"), 0),
            height = CcUtils.clampInt(ph.opt("size"), 0),
            visibility = CcJson.toStringV(baseInfo.opt("visibilityType")),
        )
    }

    fun inferableGridIndices(buttons: List<JsonObject>): Set<Int> {
        val buckets = linkedMapOf<GridSignature, MutableList<Int>>()
        for ((i, button) in buttons.withIndex()) {
            val text = CcJson.toStringV(button.opt("text"))
            if (CcEvents.fclButtonHasPayload(button) || text.trim().isEmpty()) continue
            val sig = fclButtonGridSignature(button)
            if (sig.width <= 0 || sig.height <= 0) continue
            buckets.getOrPut(sig) { mutableListOf() }.add(i)
        }
        val result = linkedSetOf<Int>()
        for (indices in buckets.values) {
            if (indices.size >= 4) result.addAll(indices)
        }
        return result
    }

    private data class GroupMatch(
        val prefixScore: Long,
        val normalizedLen: Int,
        val candidateLen: Int,
        val groupId: String,
    )

    fun inferEventsFromGroupNames(
        button: JsonObject,
        groupIdsByName: LinkedHashMap<String, String>,
        groupName: String,
    ): List<JsonObject> {
        val text = CcJson.toStringV(button.opt("text"))
        val textWords = CcUtils.normalizedControlWords(text)
        val normalizedText = CcUtils.normalizedControlText(text)
        if (textWords.isEmpty() && normalizedText.isEmpty()) return emptyList()

        val matches = mutableListOf<GroupMatch>()
        val groupPrefix = CcUtils.normalizedControlText(groupName)
        for ((candidateName, groupId) in groupIdsByName) {
            if (groupId.isEmpty() || candidateName == groupName) continue
            val candidateWords = CcUtils.normalizedControlWords(candidateName)
            val normalizedCandidate = CcUtils.normalizedControlText(candidateName)
            if (candidateWords.isEmpty() && normalizedCandidate.isEmpty()) continue
            val candidateSubset = candidateWords.isNotEmpty() && textWords.containsAll(candidateWords)
            if (candidateSubset || (normalizedCandidate.isNotEmpty() && normalizedText.contains(normalizedCandidate))) {
                var prefixScore = 0L
                if (groupPrefix.isNotEmpty() && normalizedCandidate.startsWith(groupPrefix)) prefixScore = 1L
                matches.add(GroupMatch(prefixScore, normalizedCandidate.length, candidateName.length, groupId))
            }
        }

        // cc.py：matches.sort(reverse=True)（四元组整体比较，含 group_id；稳定排序）
        matches.sortWith(
            compareByDescending<GroupMatch> { it.prefixScore }
                .thenByDescending { it.normalizedLen }
                .thenByDescending { it.candidateLen }
                .thenByDescending { it.groupId }
        )
        if (matches.isEmpty()) return emptyList()
        return CcUtils.dedupeEvents(
            listOf(CcJson.obj("type" to CcJson.str("switch_layer"), "key" to CcJson.str(matches[0].groupId)))
        )
    }

    fun inferBuiltinMenuEvents(button: JsonObject): List<JsonObject> {
        val text = CcUtils.normalizedControlText(CcJson.toStringV(button.opt("text")))
        return when (text) {
            "fcl菜单", "菜单" -> listOf(
                CcJson.obj("type" to CcJson.str("launcher_event"), "key" to CcJson.str("launcher.event.switch_menu"))
            )
            "输入法", "输入文字" -> listOf(
                CcJson.obj("type" to CcJson.str("launcher_event"), "key" to CcJson.str("launcher.event.switch_ime"))
            )
            "社交" -> listOf(CcJson.obj("type" to CcJson.str("key"), "key" to CcJson.str("GLFW_KEY_P")))
            "聊天" -> listOf(CcJson.obj("type" to CcJson.str("key"), "key" to CcJson.str("GLFW_KEY_T")))
            else -> emptyList()
        }
    }

    fun eventBindTargets(button: JsonObject): Set<String> {
        val targets = linkedSetOf<String>()
        val eventRoot = button.optObj("event") ?: JsonObject()
        for (eventName in CcEvents.EVENT_NAMES) {
            val event = eventRoot.optObj(eventName) ?: JsonObject()
            for (groupId in event.optArr("bindViewGroup") ?: emptyList()) {
                targets.add(CcJson.toStringV(groupId))
            }
        }
        return targets
    }

    fun layerEventTargets(group: JsonObject): Set<String> {
        val targets = linkedSetOf<String>()
        val viewData = group.optObj("viewData") ?: JsonObject()
        for (item in viewData.optArr("buttonList") ?: emptyList()) {
            val obj = item.asObjOrNull() ?: continue
            targets.addAll(eventBindTargets(obj))
        }
        return targets
    }

    /** 几何就近配对「开/关面板」按钮：返回 (装饰按钮 id -> 目标图层 id)。 */
    fun inferReciprocalLayerOpeners(data: JsonObject, aspect: Double): Map<String, String> {
        val groups = (data.optArr("viewGroups") ?: emptyList()).mapNotNull { it.asObjOrNull() }
        val openerScores = linkedMapOf<String, Pair<Double, String>>()

        val groupIndex = linkedMapOf<String, Int>()
        groups.forEachIndexed { index, group -> groupIndex[CcJson.toStringV(group.opt("id"))] = index }
        val groupIdsByName = LinkedHashMap<String, String>()
        for (group in groups) {
            val id = CcJson.toStringV(group.opt("id"))
            if (id.isEmpty()) continue
            val rawName = group.opt("name")
            val name = if (CcUtils.pyTruthy(rawName)) CcJson.toStringV(rawName) else "Layer"
            groupIdsByName[name] = id
        }
        val targetsByGroupId = linkedMapOf<String, Set<String>>()
        for (group in groups) {
            targetsByGroupId[CcJson.toStringV(group.opt("id"))] = layerEventTargets(group)
        }

        for (sourceGroup in groups) {
            val sourceId = CcJson.toStringV(sourceGroup.opt("id"))
            val viewData = sourceGroup.optObj("viewData") ?: JsonObject()
            val sourceButtons = (viewData.optArr("buttonList") ?: emptyList()).mapNotNull { it.asObjOrNull() }
            val candidates = sourceButtons.filter { button ->
                val text = CcJson.toStringV(button.opt("text"))
                !CcEvents.fclButtonHasPayload(button) && text.trim().isNotEmpty() &&
                    fclButtonAreaRatio(button, aspect) < 0.05
            }
            if (candidates.isEmpty()) continue

            val sourceRawName = sourceGroup.opt("name")
            val sourceName = if (CcUtils.pyTruthy(sourceRawName)) CcJson.toStringV(sourceRawName) else ""
            for (candidate in candidates) {
                val inferredEvents = inferEventsFromGroupNames(candidate, groupIdsByName, sourceName)
                for (event in inferredEvents) {
                    val targetId = CcJson.toStringV(event.opt("key"))
                    if (targetId.isNotEmpty() && targetId != sourceId) {
                        val indexDistance = Math.abs(
                            (groupIndex[targetId] ?: 0).toLong() - (groupIndex[sourceId] ?: 0).toLong()
                        )
                        val buttonId = CcJson.toStringV(candidate.opt("id"))
                        val score = indexDistance * 10000.0 - 1.0
                        val previous = openerScores[buttonId]
                        if (previous == null || score < previous.first) {
                            openerScores[buttonId] = Pair(score, targetId)
                        }
                    }
                }
            }

            for (targetGroup in groups) {
                val targetId = CcJson.toStringV(targetGroup.opt("id"))
                if (targetId.isEmpty() || targetId == sourceId) continue
                if (CcJson.toStringV(targetGroup.opt("visibility")) != "INVISIBLE") continue
                val targetRawName = targetGroup.opt("name")
                val targetName = if (CcUtils.pyTruthy(targetRawName)) CcJson.toStringV(targetRawName) else ""
                val sourceWords = CcUtils.normalizedControlWords(sourceName)
                val targetWords = CcUtils.normalizedControlWords(targetName)
                val sourceTargets = targetsByGroupId[sourceId]
                val hasIntersection = sourceWords.isNotEmpty() && targetWords.isNotEmpty() &&
                    sourceWords.any { targetWords.contains(it) }
                if (hasIntersection && (sourceTargets == null || targetId !in sourceTargets)) continue

                val targetViewData = targetGroup.optObj("viewData") ?: JsonObject()
                val targetButtons = (targetViewData.optArr("buttonList") ?: emptyList()).mapNotNull { it.asObjOrNull() }
                val closeButtons = targetButtons.filter { button ->
                    val bindTargets = eventBindTargets(button)
                    val ratio = fclButtonAreaRatio(button, aspect)
                    sourceId in bindTargets && targetId in bindTargets && ratio in 0.08..0.50
                }
                if (closeButtons.isEmpty()) continue

                var bestCandidate: JsonObject? = null
                var bestDistance = Double.POSITIVE_INFINITY
                for (candidate in candidates) {
                    val candidateRect = fclButtonRect(candidate, aspect)
                    for (closeButton in closeButtons) {
                        val distance = rectDistance(candidateRect, fclButtonRect(closeButton, aspect))
                        if (distance < bestDistance) {
                            bestDistance = distance
                            bestCandidate = candidate
                        }
                    }
                }
                val best = bestCandidate
                if (best != null && bestDistance <= 500.0) {
                    val buttonId = CcJson.toStringV(best.opt("id"))
                    val indexDistance = Math.abs(
                        (groupIndex[targetId] ?: 0).toLong() - (groupIndex[sourceId] ?: 0).toLong()
                    )
                    val score = indexDistance * 10000.0 + bestDistance
                    val previous = openerScores[buttonId]
                    if (previous == null || score < previous.first) {
                        openerScores[buttonId] = Pair(score, targetId)
                    }
                }
            }
        }
        val result = linkedMapOf<String, String>()
        for ((buttonId, pair) in openerScores) result[buttonId] = pair.second
        return result
    }
}
