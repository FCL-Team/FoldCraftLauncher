## 变更说明

<!-- 简述本 PR 的改动与动机 -->

## 检查清单

- [ ] 已阅读 `docs/migration/final-report.md` 的迁移约定（如涉 UI）
- [ ] `./gradlew :FCL:assembleDebug` 构建通过
- [ ] **API 密钥提醒**：本地验证微软登录 / CurseForge 功能时，需在 `local.properties` 配置 `oauth.api.key` / `curse.api.key`（CI 由 GitHub Secrets 自动注入，PR 合并后发布的包不受影响；本地未配置时微软登录会报 `AADSTS700016 client_id=null`，属配置问题而非代码缺陷）
- [ ] 涉及已迁移页面/弹窗时，已核对 `USE_COMPOSE_*` 开关双分支均可用

## 测试情况

<!-- 真机/模拟器验证项；无设备时说明原因 -->
