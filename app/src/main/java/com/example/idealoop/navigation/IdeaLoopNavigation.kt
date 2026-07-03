package com.example.idealoop.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.idealoop.feature.account.AccountContract
import com.example.idealoop.feature.account.PhoneLoginScreen
import com.example.idealoop.feature.account.ProfileSetupScreen
import com.example.idealoop.feature.account.VerificationScreen
import com.example.idealoop.feature.awake.AwakeContract
import com.example.idealoop.feature.awake.AwakeListScreen
import com.example.idealoop.feature.awake.LocationAwakeDetailScreen
import com.example.idealoop.feature.awake.TimeAwakeDetailScreen
import com.example.idealoop.feature.capture.CaptureContract
import com.example.idealoop.feature.capture.CaptureAnalysisScreen
import com.example.idealoop.feature.capture.CaptureInputKind
import com.example.idealoop.feature.capture.CaptureSession
import com.example.idealoop.feature.capture.ChatMessageContent
import com.example.idealoop.feature.capture.ChatMode
import com.example.idealoop.feature.capture.ChatPhotosResultScreen
import com.example.idealoop.feature.capture.ChatVoiceResultScreen
import com.example.idealoop.feature.capture.AlbumPickerScreen
import com.example.idealoop.feature.capture.PendingMemoryScreen
import com.example.idealoop.feature.capture.RecordVoiceAnalysisScreen
import com.example.idealoop.feature.capture.SaveSuccessDialogContent
import com.example.idealoop.feature.capture.XiaolingChatScreen
import com.example.idealoop.feature.capture.captureDestination
import com.example.idealoop.feature.home.HomeCallbacks
import com.example.idealoop.feature.home.HomeContract
import com.example.idealoop.feature.home.HomeScreen
import com.example.idealoop.feature.memory.MemoryContract
import com.example.idealoop.feature.memory.MemoryDetailScreen
import com.example.idealoop.feature.memory.MemoryLibraryCallbacks
import com.example.idealoop.feature.memory.MemoryLibraryScreen
import com.example.idealoop.feature.memory.MemorySampleData
import com.example.idealoop.feature.profile.EmptyMemoryStateScreen
import com.example.idealoop.feature.profile.ErrorStateScreen
import com.example.idealoop.feature.profile.LogoutConfirmationDialogContent
import com.example.idealoop.feature.profile.PermissionsScreen
import com.example.idealoop.feature.profile.ProfileCallbacks
import com.example.idealoop.feature.profile.ProfileContract
import com.example.idealoop.feature.profile.ProfileScreen
import com.example.idealoop.feature.review.ActionSuggestionsScreen
import com.example.idealoop.feature.review.ReviewContract
import com.example.idealoop.feature.review.ReviewThemesScreen
import com.example.idealoop.feature.review.SuggestionMemoriesScreen
import com.example.idealoop.feature.review.ThemeMemoriesScreen
import com.example.idealoop.feature.review.WeeklyReviewScreen
import com.example.idealoop.feature.search.SearchContract
import com.example.idealoop.feature.search.SearchInputScreen
import com.example.idealoop.feature.search.SearchResultsCallbacks
import com.example.idealoop.feature.search.SearchResultsScreen
import com.example.idealoop.feature.search.XiaolingRelatedScreen
import com.example.idealoop.ui.components.IdeaLoopBottomItem

@Composable
fun IdeaLoopNavHost(
    modifier: Modifier = Modifier,
    homeCallbacks: HomeCallbacks = HomeCallbacks(),
) {
    val navController = rememberNavController()
    val captureSession = remember { CaptureSession() }
    val navigateBottomItem: (IdeaLoopBottomItem) -> Unit = { item ->
        when (item) {
            IdeaLoopBottomItem.Home -> navController.navigate(HomeContract.route) {
                popUpTo(HomeContract.route) { inclusive = false }
                launchSingleTop = true
            }
            IdeaLoopBottomItem.Memory -> navController.navigate(MemoryContract.libraryRoute) {
                launchSingleTop = true
            }
            IdeaLoopBottomItem.Review -> navController.navigate(ReviewContract.overviewRoute) {
                launchSingleTop = true
            }
            IdeaLoopBottomItem.Profile -> navController.navigate(ProfileContract.profileRoute) {
                launchSingleTop = true
            }
        }
    }
    val returnTo: (String) -> Unit = { route ->
        if (!navController.popBackStack(route, inclusive = false)) {
            navController.navigate(route) { launchSingleTop = true }
        }
    }
    val exitCaptureFlow: () -> Unit = {
        navController.popBackStack(CaptureContract.route, inclusive = true)
    }

    NavHost(
        navController = navController,
        startDestination = AccountContract.phoneRoute,
        modifier = modifier,
    ) {
        composable(AccountContract.phoneRoute) {
            PhoneLoginScreen(
                onContinue = {
                    navController.navigate(AccountContract.verificationRoute)
                },
            )
        }
        composable(AccountContract.verificationRoute) {
            VerificationScreen(
                onBack = { navController.popBackStack() },
                onContinue = {
                    navController.navigate(AccountContract.profileRoute)
                },
            )
        }
        composable(AccountContract.profileRoute) {
            ProfileSetupScreen(
                onBack = { navController.popBackStack() },
                onContinue = {
                    navController.navigate(HomeContract.route)
                },
            )
        }
        composable(HomeContract.route) {
            HomeScreen(
                callbacks = homeCallbacks.copy(
                    onAdd = {
                        homeCallbacks.onAdd()
                        navController.navigate(CaptureContract.route)
                    },
                    onSearch = {
                        homeCallbacks.onSearch()
                        navController.navigate(SearchContract.inputRoute)
                    },
                    onActionSuggestions = {
                        homeCallbacks.onActionSuggestions()
                        navController.navigate(ReviewContract.suggestionsRoute)
                    },
                    onRecentMemory = { memory ->
                        homeCallbacks.onRecentMemory(memory)
                        navController.navigate(MemoryContract.detailRoute(memory.id))
                    },
                    onAwakeAll = {
                        homeCallbacks.onAwakeAll()
                        navController.navigate(AwakeContract.listRoute)
                    },
                    onAwakeCard = { card ->
                        homeCallbacks.onAwakeCard(card)
                        val route = if (card.id == "a2") {
                            AwakeContract.timeRoute
                        } else {
                            AwakeContract.locationRoute
                        }
                        navController.navigate(route)
                    },
                    onBottomItem = { item ->
                        homeCallbacks.onBottomItem(item)
                        when (item) {
                            IdeaLoopBottomItem.Memory,
                            IdeaLoopBottomItem.Review,
                            IdeaLoopBottomItem.Profile,
                            -> navigateBottomItem(item)
                            else -> Unit
                        }
                    },
                ),
            )
        }
        composable(AwakeContract.listRoute) {
            AwakeListScreen(
                onBack = { navController.popBackStack() },
                onItem = { item ->
                    navController.navigate(AwakeContract.routeFor(item.trigger))
                },
            )
        }
        composable(AwakeContract.locationRoute) {
            LocationAwakeDetailScreen(
                onBack = { navController.popBackStack() },
                onMemory = { memory ->
                    navController.navigate(MemoryContract.detailRoute(memory.id))
                },
            )
        }
        composable(AwakeContract.timeRoute) {
            TimeAwakeDetailScreen(
                onBack = { navController.popBackStack() },
                onMemory = { memory ->
                    navController.navigate(MemoryContract.detailRoute(memory.id))
                },
            )
        }
        composable(MemoryContract.libraryRoute) {
            MemoryLibraryScreen(
                callbacks = MemoryLibraryCallbacks(
                    onAdd = {
                        navController.navigate(CaptureContract.route)
                    },
                    onSearch = {
                        navController.navigate(SearchContract.inputRoute)
                    },
                    onMemory = { memory ->
                        navController.navigate(MemoryContract.detailRoute(memory.id))
                    },
                    onBottomItem = { item ->
                        when (item) {
                            IdeaLoopBottomItem.Home,
                            IdeaLoopBottomItem.Review,
                            IdeaLoopBottomItem.Profile,
                            -> navigateBottomItem(item)
                            else -> Unit
                        }
                    },
                ),
            )
        }
        composable(ReviewContract.overviewRoute) {
            WeeklyReviewScreen(
                onAllSuggestions = { navController.navigate(ReviewContract.suggestionsRoute) },
                onSuggestionMemories = { suggestion ->
                    navController.navigate(ReviewContract.relatedRoute(suggestion.id))
                },
                onAllThemes = { navController.navigate(ReviewContract.themesRoute) },
                onTheme = { theme ->
                    navController.navigate(ReviewContract.themeMemoriesRoute(theme.id))
                },
                onBottomItem = navigateBottomItem,
                onAdd = { navController.navigate(CaptureContract.route) },
            )
        }
        composable(ReviewContract.suggestionsRoute) {
            ActionSuggestionsScreen(
                onBack = { navController.popBackStack() },
                onSuggestionMemories = { suggestion ->
                    navController.navigate(ReviewContract.relatedRoute(suggestion.id))
                },
                onBottomItem = navigateBottomItem,
                onAdd = { navController.navigate(CaptureContract.route) },
            )
        }
        composable(ReviewContract.relatedRoutePattern) { backStackEntry ->
            val suggestionId = backStackEntry.arguments
                ?.getString(ReviewContract.suggestionIdArgument)
                .orEmpty()
            SuggestionMemoriesScreen(
                suggestionId = suggestionId,
                onBack = { navController.popBackStack() },
                onMemory = { memory ->
                    navController.navigate(MemoryContract.detailRoute(memory.id))
                },
                onBottomItem = navigateBottomItem,
                onAdd = { navController.navigate(CaptureContract.route) },
            )
        }
        composable(ReviewContract.themesRoute) {
            ReviewThemesScreen(
                onBack = { navController.popBackStack() },
                onTheme = { theme ->
                    navController.navigate(ReviewContract.themeMemoriesRoute(theme.id))
                },
                onBottomItem = navigateBottomItem,
                onAdd = { navController.navigate(CaptureContract.route) },
            )
        }
        composable(ReviewContract.themeMemoriesRoutePattern) { backStackEntry ->
            val themeId = backStackEntry.arguments
                ?.getString(ReviewContract.themeIdArgument)
                .orEmpty()
            ThemeMemoriesScreen(
                themeId = themeId,
                onBack = { navController.popBackStack() },
                onMemory = { memory ->
                    navController.navigate(MemoryContract.detailRoute(memory.id))
                },
                onBottomItem = navigateBottomItem,
                onAdd = { navController.navigate(CaptureContract.route) },
            )
        }
        composable(ProfileContract.profileRoute) {
            ProfileScreen(
                callbacks = ProfileCallbacks(
                    onPermissions = { navController.navigate(ProfileContract.permissionsRoute) },
                    onLogout = { navController.navigate(ProfileContract.logoutRoute) },
                    onBottomItem = navigateBottomItem,
                    onAdd = { navController.navigate(CaptureContract.route) },
                ),
            )
        }
        composable(ProfileContract.permissionsRoute) {
            PermissionsScreen(onBack = { navController.popBackStack() })
        }
        dialog(ProfileContract.logoutRoute) {
            LogoutConfirmationDialogContent(
                onCancel = { navController.popBackStack() },
                onConfirm = {
                    navController.navigate(AccountContract.phoneRoute) {
                        popUpTo(AccountContract.phoneRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(ProfileContract.emptyRoute) {
            EmptyMemoryStateScreen(
                onBack = { returnTo(MemoryContract.libraryRoute) },
                onAdd = { navController.navigate(CaptureContract.route) },
            )
        }
        composable(ProfileContract.errorRoute) {
            ErrorStateScreen(
                onRetry = { returnTo(HomeContract.route) },
                onBack = { returnTo(HomeContract.route) },
            )
        }
        composable(SearchContract.inputRoute) {
            SearchInputScreen(
                onBack = { navController.popBackStack() },
                onSearch = { query ->
                    val route = if (SearchContract.isNaturalLanguage(query)) {
                        SearchContract.xiaolingRoute(query)
                    } else {
                        SearchContract.resultsRoute(query)
                    }
                    navController.navigate(route)
                },
            )
        }
        composable(CaptureContract.route) {
            XiaolingChatScreen(
                onBack = { navController.popBackStack() },
                initialMode = captureSession.mode,
                onModeChanged = { captureSession.mode = it },
                onRecordMessage = { content ->
                    captureSession.prepare(content)
                    navController.navigate(CaptureContract.analysisRoute)
                },
                onOpenAlbum = { mode ->
                    captureSession.mode = mode
                    navController.navigate(CaptureContract.albumRoute)
                },
                onVoiceMessage = { mode ->
                    captureSession.mode = mode
                    captureSession.prepare(
                        ChatMessageContent.Voice(
                            duration = if (mode == ChatMode.Chat) "8\"" else "14\"",
                            transcript = if (mode == ChatMode.Chat) {
                                "帮我找一下广州周末旅行相关的记忆"
                            } else {
                                "这周末打算去广州，重点想去陶陶居吃早茶，再去珠江夜游"
                            },
                        ),
                    )
                    captureDestination(mode, CaptureInputKind.Voice)?.let(navController::navigate)
                },
            )
        }
        composable(CaptureContract.analysisRoute) {
            BackHandler(onBack = exitCaptureFlow)
            CaptureAnalysisScreen(
                session = captureSession,
                onBack = exitCaptureFlow,
                onFinished = {
                    navController.navigate(CaptureContract.pendingMemoryRoute) {
                        popUpTo(CaptureContract.analysisRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(CaptureContract.pendingMemoryRoute) {
            BackHandler(onBack = exitCaptureFlow)
            PendingMemoryScreen(
                session = captureSession,
                onBack = exitCaptureFlow,
                onSave = { navController.navigate(CaptureContract.savedRoute) },
                onReanalyze = {
                    navController.navigate(CaptureContract.analysisRoute) {
                        popUpTo(CaptureContract.pendingMemoryRoute) { inclusive = true }
                    }
                },
            )
        }
        dialog(CaptureContract.savedRoute) {
            SaveSuccessDialogContent(
                onClose = {
                    navController.popBackStack(CaptureContract.route, inclusive = false)
                },
            )
        }
        composable(CaptureContract.chatVoiceRoute) {
            BackHandler(onBack = exitCaptureFlow)
            ChatVoiceResultScreen(
                session = captureSession,
                onBack = exitCaptureFlow,
                onOpenAlbum = { mode ->
                    captureSession.mode = mode
                    navController.navigate(CaptureContract.albumRoute)
                },
                onVoiceMessage = { mode ->
                    captureSession.mode = mode
                    captureDestination(mode, CaptureInputKind.Voice)?.let(navController::navigate)
                },
                onRecordMessage = { content ->
                    captureSession.prepare(content)
                    navController.navigate(CaptureContract.analysisRoute)
                },
            )
        }
        composable(CaptureContract.recordVoiceRoute) {
            BackHandler(onBack = exitCaptureFlow)
            RecordVoiceAnalysisScreen(
                session = captureSession,
                onBack = exitCaptureFlow,
                onFinished = {
                    navController.navigate(CaptureContract.pendingMemoryRoute) {
                        popUpTo(CaptureContract.recordVoiceRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(CaptureContract.albumRoute) {
            AlbumPickerScreen(
                onBack = { navController.popBackStack() },
                onSend = { ids ->
                    captureSession.preparePhotos(ids)
                    val destination = captureDestination(
                        captureSession.mode,
                        CaptureInputKind.Photos,
                    ) ?: CaptureContract.chatPhotosRoute
                    navController.navigate(destination)
                },
            )
        }
        composable(CaptureContract.chatPhotosRoute) {
            BackHandler(onBack = exitCaptureFlow)
            ChatPhotosResultScreen(
                session = captureSession,
                onBack = exitCaptureFlow,
                onMemory = { memory ->
                    navController.navigate(MemoryContract.detailRoute(memory.id))
                },
                onOpenAlbum = { mode ->
                    captureSession.mode = mode
                    navController.navigate(CaptureContract.albumRoute)
                },
                onVoiceMessage = { mode ->
                    captureSession.mode = mode
                    captureDestination(mode, CaptureInputKind.Voice)?.let(navController::navigate)
                },
                onRecordMessage = { content ->
                    captureSession.prepare(content)
                    navController.navigate(CaptureContract.analysisRoute)
                },
            )
        }
        composable(SearchContract.resultsRoutePattern) { backStackEntry ->
            val query = SearchContract.decodeQuery(
                backStackEntry.arguments
                    ?.getString(SearchContract.queryArgument)
                    .orEmpty(),
            )
            SearchResultsScreen(
                query = query,
                callbacks = SearchResultsCallbacks(
                    onBack = { navController.popBackStack() },
                    onMemory = { memory ->
                        navController.navigate(MemoryContract.detailRoute(memory.id))
                    },
                    onAdd = {
                        navController.navigate(CaptureContract.route)
                    },
                    onBottomItem = { item ->
                        navigateBottomItem(item)
                    },
                ),
            )
        }
        composable(SearchContract.xiaolingRoutePattern) { backStackEntry ->
            val query = SearchContract.decodeQuery(
                backStackEntry.arguments
                    ?.getString(SearchContract.queryArgument)
                    .orEmpty(),
            )
            XiaolingRelatedScreen(
                query = query,
                onBack = { navController.popBackStack() },
                onMemory = { memory ->
                    navController.navigate(MemoryContract.detailRoute(memory.id))
                },
                initialMode = captureSession.mode,
                onModeChanged = { captureSession.mode = it },
                onRecordMessage = { content ->
                    captureSession.prepare(content)
                    navController.navigate(CaptureContract.analysisRoute)
                },
                onOpenAlbum = { mode ->
                    captureSession.mode = mode
                    navController.navigate(CaptureContract.albumRoute)
                },
                onVoiceMessage = { mode ->
                    captureSession.mode = mode
                    captureSession.prepare(
                        ChatMessageContent.Voice(
                            duration = if (mode == ChatMode.Chat) "8\"" else "14\"",
                            transcript = if (mode == ChatMode.Chat) {
                                "帮我找一下广州周末旅行相关的记忆"
                            } else {
                                "这周末打算去广州，重点想去陶陶居吃早茶，再去珠江夜游"
                            },
                        ),
                    )
                    captureDestination(mode, CaptureInputKind.Voice)?.let(navController::navigate)
                },
            )
        }
        composable(MemoryContract.detailRoutePattern) { backStackEntry ->
            val memoryId = backStackEntry.arguments
                ?.getString(MemoryContract.memoryIdArgument)
                .orEmpty()
            MemoryDetailScreen(
                detail = MemorySampleData.detail(memoryId),
                onBack = { navController.popBackStack() },
                onRelatedMemory = { memory ->
                    navController.navigate(MemoryContract.detailRoute(memory.id))
                },
            )
        }
    }
}
