import { ref, computed, watchEffect, onMounted } from 'vue'
import { darkTheme } from 'naive-ui'
import type { GlobalTheme } from 'naive-ui'
import { lightThemeOverrides, darkThemeOverrides } from '@/styles/theme'

export type ThemeMode = 'light' | 'dark' | 'system'

const STORAGE_KEY = 'cet4-theme-mode'

// 全局状态
const mode = ref<ThemeMode>('system')

export function useTheme() {
  const systemDark = ref(false)

  // 检测系统主题
  const updateSystemTheme = () => {
    systemDark.value = window.matchMedia('(prefers-color-scheme: dark)').matches
  }

  onMounted(() => {
    // 从 localStorage 恢复主题设置
    const saved = localStorage.getItem(STORAGE_KEY) as ThemeMode | null
    if (saved && ['light', 'dark', 'system'].includes(saved)) {
      mode.value = saved
    }

    // 监听系统主题变化
    updateSystemTheme()
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', updateSystemTheme)
  })

  // 计算当前是否为深色模式
  const isDark = computed(() => {
    if (mode.value === 'system') {
      return systemDark.value
    }
    return mode.value === 'dark'
  })

  // Naive UI 主题对象
  const theme = computed<GlobalTheme | null>(() => isDark.value ? darkTheme : null)

  // Naive UI 主题覆盖
  const themeOverrides = computed(() => 
    isDark.value ? darkThemeOverrides : lightThemeOverrides
  )

  // 设置主题模式
  function setMode(newMode: ThemeMode): void {
    mode.value = newMode
    localStorage.setItem(STORAGE_KEY, newMode)
  }

  // 切换主题
  function toggleTheme(): void {
    if (mode.value === 'system') {
      setMode(isDark.value ? 'light' : 'dark')
    } else {
      setMode(mode.value === 'dark' ? 'light' : 'dark')
    }
  }

  // 同步 data-theme 属性到 document
  watchEffect(() => {
    document.documentElement.setAttribute('data-theme', isDark.value ? 'dark' : 'light')
  })

  return {
    mode,
    isDark,
    theme,
    themeOverrides,
    setMode,
    toggleTheme
  }
}
