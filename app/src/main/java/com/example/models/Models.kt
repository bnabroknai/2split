package com.example.models

data class Device(
    val id: String,
    val label: String,
    val icon: String
)

data class Method(
    val title: String,
    val badge: String,
    val badgeColorHex: String,
    val steps: List<String>,
    val shortcut: String,
    val shortcutNote: String,
    val tip: String
)

val devices = listOf(
    Device("chromebook", "Chromebook", "💻"),
    Device("android", "Android", "📱"),
    Device("iphone", "iPhone / iPad", "🍎"),
    Device("workflow", "AI Flow Tricks", "⚡")
)

val methods = mapOf(
    "chromebook" to listOf(
        Method(
            "Native Snap (Built-in)",
            "BEST OPTION",
            "#00ff9d",
            listOf(
                "Hover over the ⬜ maximize button on any Chrome window",
                "Hold it — a snap menu appears (left / right / full)",
                "Choose left → then pick your second window for the right",
                "Repeat to set ChatGPT left, Copilot right"
            ),
            "Alt + [ or Alt + ]",
            "Keyboard shortcut to snap active window",
            "Works across any two browser windows, not just tabs."
        ),
        Method(
            "Tab Resize Extension",
            "POWER USER",
            "#ffaa00",
            listOf(
                "Install 'Tab Resize' from Chrome Web Store (free)",
                "Click the extension icon → pick a layout (50/50, 70/30, etc.)",
                "Select which tabs fill which pane",
                "Layouts save — click once to restore your setup"
            ),
            "chrome.google.com/webstore → 'Tab Resize'",
            "Search: Tab Resize – split screen layouts",
            "⚠️ ChatGPT & Copilot block iframes, so use separate windows — not embedded frames."
        ),
        Method(
            "Virtual Desks",
            "CONTEXT SWITCHER",
            "#7b8cde",
            listOf(
                "Press Shift + Search + = to create a new Desk",
                "Put your AI tools on Desk 1, your work doc on Desk 2",
                "Swipe 3-finger left/right to switch desks instantly",
                "Each desk holds its own window arrangement"
            ),
            "Search + ] or [ to jump between desks",
            "Like having multiple monitors on one screen",
            "Pair with split screen on each desk for a full multi-context setup."
        )
    ),
    "android" to listOf(
        Method(
            "Native Split Screen",
            "BUILT-IN",
            "#00ff9d",
            listOf(
                "Open your first app (e.g. ChatGPT)",
                "Tap the ⬜ Recents/Overview button (3-line or gesture swipe up + hold)",
                "Tap the app icon at the top of the card",
                "Choose 'Split screen' → pick the second app from the list"
            ),
            "Long-press recent app thumbnail → Split Screen",
            "Varies slightly by Android version / manufacturer",
            "Drag the divider bar to adjust pane sizes. Works with Chrome tabs as separate apps."
        ),
        Method(
            "Samsung DeX / Flex Mode",
            "SAMSUNG ONLY",
            "#ffaa00",
            listOf(
                "On Samsung: Settings → Advanced Features → DeX",
                "Enables floating windows you can freely resize",
                "Drag any app into a floating window",
                "Layer ChatGPT + Copilot + your notes simultaneously"
            ),
            "Fold phones: fold halfway to auto-enter Flex Mode",
            "Non-Samsung: look for 'Floating Windows' in settings",
            "DeX connected to a monitor = full desktop-class multi-window."
        ),
        Method(
            "Chrome Custom Tabs / Side Panel",
            "BROWSER HACK",
            "#7b8cde",
            listOf(
                "Open Chrome → navigate to ChatGPT",
                "Tap ⋮ → 'Open in split screen view' (Android 12+)",
                "Open Copilot in the second pane via address bar",
                "Both live side by side in one browser session"
            ),
            "Works natively in Chrome for Android",
            "No extension needed on Android 12+",
            "Pinch the divider to make your output pane larger when reading."
        )
    ),
    "iphone" to listOf(
        Method(
            "iPhone: No Native Split Screen",
            "LIMITATION",
            "#ff4444",
            listOf(
                "iPhones don't support split screen — Apple restricts this to iPad",
                "Best workaround: use Safari's Tab Groups to organize AI sessions",
                "Swipe between tabs with a 4-finger horizontal swipe",
                "Set up a Tab Group called 'AI Flow' with ChatGPT + Copilot pre-loaded"
            ),
            "Swipe left/right with 4 fingers to switch apps",
            "Fastest iPhone tab-switch method",
            "The Shortcuts app can create a one-tap action that opens both in sequence."
        ),
        Method(
            "iPad: Full Split View",
            "IPAD ONLY",
            "#00ff9d",
            listOf(
                "Open an app → swipe up from the bottom edge slowly (Stage Manager)",
                "Drag a second app from the dock into the same window",
                "Or: tap ••• at the top of any app → Split View → pick second app",
                "Slide Over: swipe in from the right for a floating panel"
            ),
            "Stage Manager (Settings → Multitasking)",
            "Enables floating, overlapping windows on iPad",
            "Stage Manager on iPad = closest thing to desktop multitasking Apple offers."
        ),
        Method(
            "iPhone + Chromebook Together",
            "CROSS-DEVICE",
            "#ffaa00",
            listOf(
                "Use your iPhone as input (voice to text → copy)",
                "AirDrop or iCloud clipboard to pick up on Chromebook",
                "Or: use a shared notes app (Notion, Bear) as the relay layer",
                "Phone generates → laptop edits → publish"
            ),
            "Universal Clipboard: copy on phone, paste on Mac/Chromebook",
            "Notion or Google Keep work as cross-device relay",
            "This is actually a strong workflow split: mobile for capture, laptop for refinement."
        )
    ),
    "workflow" to listOf(
        Method(
            "The Relay Method (Current Setup)",
            "YOUR SETUP",
            "#7b8cde",
            listOf(
                "ChatGPT generates → you copy → Copilot refines",
                "Problem: context gets lost in manual handoffs",
                "Fix: keep a 'Context Block' — a sticky note with your running task brief",
                "Paste it at the start of every new AI session to re-anchor"
            ),
            "Clipboard Manager (e.g. Clipboard History Pro for Chrome)",
            "Saves your last 50+ clipboard items — no more lost outputs",
            "A clipboard manager alone cuts your friction in half."
        ),
        Method(
            "Prompt Router App (Build It)",
            "BUILDABLE",
            "#00ff9d",
            listOf(
                "A simple web app that sends one prompt to multiple AI APIs simultaneously",
                "Displays outputs side by side for comparison",
                "Works because APIs don't block iframes — only websites do",
                "We can build this — it uses Claude + OpenAI APIs in parallel"
            ),
            "Ask me: 'Build me a dual-AI prompt router'",
            "Needs API keys for ChatGPT + Claude (or others)",
            "This is the actual solution to your workflow problem — one input, multiple outputs, no tab switching."
        ),
        Method(
            "Perplexity as the Bridge",
            "NO CODE",
            "#ffaa00",
            listOf(
                "Perplexity can search + synthesize in one shot",
                "Use it instead of doing ChatGPT → Copilot for research tasks",
                "For creative tasks: Claude for generation, Copilot for Office integration",
                "Reserve tab-switching for when you actually need two different model personalities"
            ),
            "perplexity.ai — has a focus mode for different task types",
            "Cuts out the middleman for research-heavy relay flows",
            "Map your task types: Research → Perplexity. Draft → Claude. Polish → Copilot."
        ),
        Method(
            "The Context Block Template",
            "WORKFLOW HACK",
            "#ff4444",
            listOf(
                "Create a reusable context snippet you paste into every AI session",
                "Include: your goal, constraints, output format, where you left off",
                "Store it in a pinned note or clipboard manager",
                "Paste → prompt → done. No re-explaining every session."
            ),
            "Text expander: type ';;ctx' → auto-expands your context block",
            "Tools: Espanso (free), TextExpander, or built-in Android snippets",
            "This is the single highest-leverage change for multi-AI workflows."
        )
    )
)
