# Idea Loop Android UI 开发约定

本文件适用于仓库中的所有 UI 开发任务。项目采用渐进方式实现：每次只完成用户当前指定的页面、组件或交互，不要求一次性完成整套 App。

## 1. 任务范围

- 以用户当前任务为边界，只修改完成该任务所必需的文件。
- 开始前先阅读相关页面、主题、组件和现有改动，沿用已经建立的实现方式。
- 不提前实现尚未要求的页面、后端、数据库、真实 AI、定位、通知或其他系统能力。
- 当前未接入的业务数据使用稳定的本地模拟数据，保证页面可以独立预览和交互。
- 不为了单个页面进行无关架构重构，也不覆盖或回退用户已有改动。

## 2. 严格参照 Figma

页面实现必须严格参照最新 Figma 设计稿 [Idea Loop version2](https://www.figma.com/make/B4smdqMwXCh80SEPpTH77D/Idea-Loop-version2?t=TUElMMr0pCNDwfCM-1)。

设计依据优先级：

1. 用户在当前任务中的明确要求。
2. 最新 Figma 设计稿。
3. `D:\AIGC\UI\Idea Loop (1) (1) (2) (1).zip` 中的 Figma Make 导出代码。

实现要求：

- 严格还原 Figma 中的信息层级、布局、尺寸比例、间距、颜色、字体层级、圆角、边框、阴影、图标、图片和文案。
- 严格还原 Figma 表达的点击、返回、弹窗、滚动、横向滑动、固定标题栏、底部菜单栏和页面跳转。
- 不随意改版，不自行增加、删除、合并页面内容，不擅自修改文案、颜色、交互或导航关系。
- Figma 未展示的细节优先沿用项目已有组件和 Android 常规行为，不添加装饰性设计。
- 无法访问设计稿、参考内容冲突或关键细节无法判断时，先说明不确定项，不凭想象补全。
- Figma Make 导出的 React、HTML 和 CSS 只用于理解设计，不能直接复制到 Android 工程。

## 3. Android 技术底线

- 使用 Kotlin、Jetpack Compose 和 Material 3 实现 UI。
- 最低支持 Android 8.0 Oreo（API 26），对应 `minSdk = 26`；未经用户明确确认不得提高最低系统版本。
- 使用 API 26 以上才提供的系统能力时，增加版本判断和兼容或降级行为。
- `targetSdk`、Java 版本和其他构建参数以当前 Gradle 配置为准。
- 保持当前 `applicationId` 和包名 `com.example.idealoop`，除非用户明确要求修改。
- 依赖统一通过 `gradle/libs.versions.toml` 管理，不在模块构建文件中散落版本号。
- 仅在当前功能确实需要时添加依赖，不一次性引入所有候选库。

## 4. 通用 UI 要求

- 这是 Android App，不使用 iOS 控件、iPhone 状态栏、Home Indicator 或 iOS 导航样式。
- 使用真实、自然的中文内容，不使用无意义占位文本。
- 正确处理状态栏、导航栏、键盘和 edge-to-edge inset，避免内容被系统区域遮挡。
- 页面内容可以完整滚动，固定标题栏和菜单栏是否跟随滚动以 Figma 为准。
- 文字、图标、标签、按钮和卡片不得重叠、溢出、异常截断或撑破容器。
- 至少检查 360dp 与 412dp 宽度下的布局表现，不通过连续缩放字体解决适配问题。
- 优先复用主题 token 和已有组件；只在确有重复需求时新增共享组件。
- 图标优先使用项目现有资源或 Material Icons；Figma 提供专用资源时使用设计稿资源。
- 底部主导航保持为 `首页 / 记忆 / + / 复盘 / 我的`，除非当前任务明确要求调整。
- 视觉效果服从 Figma，不额外增加强渐变、重阴影、大光晕或过度玻璃效果。

## 5. 渐进式实现流程

每次 UI 任务按以下顺序处理：

1. 确认目标页面、相关状态和跳转入口。
2. 对照 Figma 检查布局、视觉资源、滚动行为和交互。
3. 只实现当前页面及其必要的共享组件或导航连接。
4. 使用本地模拟数据提供可查看的正常状态；任务涉及空状态、错误状态时再补充对应状态。
5. 为目标 Screen 或关键组件提供有意义的 `@Preview`，便于快速对照设计。
6. 在常见 Android 手机尺寸下检查视觉效果，并验证本次涉及的点击和返回路径。
7. 运行与改动范围匹配的检查；至少保证 Debug 构建成功。

测试和抽象的范围应与当前任务风险相匹配。简单视觉调整不要求补齐整套测试，新增状态逻辑或关键导航时再增加相应单元测试或 Compose UI 测试。

常用命令：

```powershell
# 构建 Debug APK
.\gradlew.bat assembleDebug

# 当前改动涉及状态逻辑时运行
.\gradlew.bat testDebugUnitTest

# 当前改动涉及设备交互且已有模拟器或设备时运行
.\gradlew.bat connectedDebugAndroidTest
```

## 6. 可选 UI 库与官方地址

以下库仅作为按需选型入口，不代表必须添加：

| 库或工具 | 适用场景 | 官方地址 |
| --- | --- | --- |
| Coil Compose | 加载网络图片、相册 URI、缩略图和图片加载状态 | [Coil Compose](https://coil-kt.github.io/coil/compose/) |
| Navigation Compose | 页面路由、返回栈和参数传递 | [Navigation with Compose](https://developer.android.com/develop/ui/compose/navigation) |
| Lifecycle / ViewModel Compose | 页面状态与生命周期管理 | [Compose and other libraries](https://developer.android.com/develop/ui/compose/libraries#viewmodel) |
| Material 3 for Compose | Android 组件、主题和交互基线 | [Material 3 in Compose](https://developer.android.com/develop/ui/compose/designsystems/material3) |
| Lottie Compose | 设计稿明确提供 Lottie 资源时渲染动画 | [Lottie Android](https://github.com/airbnb/lottie-android) |
| Compose Preview Screenshot Testing | 对稳定页面进行可选的视觉回归测试 | [Compose Preview Screenshot Testing](https://developer.android.com/studio/preview/compose-screenshot-testing) |

引入库前查看官方文档和发布说明，确认与当前 Compose、Kotlin、AGP 及 Android 8.0 兼容。第三方库只解决具体问题，不得替代 Figma 的视觉要求。

## 7. 文件与 Git

- 仓库只保留源码、必要资源、配置、测试和项目文档。
- 不提交或保留无关的编译产物、缓存和本机配置，包括 `.gradle/`、`.kotlin/`、`**/build/`、`*.apk`、`*.aab`、`local.properties` 和 IDE 工作区状态。
- 任务中临时生成的日志、截图、导出文件、解压目录和中间产物，如果不是交付内容，完成前删除。
- 新工具会生成额外文件时同步维护 `.gitignore`，避免无关文件出现在 `git status` 中。
- 清理文件时不得删除用户原有资料或未提交改动。
- 不使用 `git reset --hard`、强制推送等破坏性命令。
- 未经用户明确要求，不创建提交、不推送远程仓库。

## 8. 每次任务的完成条件

- 当前指定页面、组件或交互已经完成，不以未要求的其他页面作为阻塞项。
- 实现与 Figma 及用户当前要求一致，没有未经确认的设计发挥。
- 本次涉及的内容在目标尺寸下没有明显重叠、溢出或系统栏遮挡。
- 本次相关的交互路径可以正常操作。
- `assembleDebug` 成功；未运行的设备测试在结果中如实说明。
- Git diff 只包含当前任务相关文件，没有编译产物或临时文件。
