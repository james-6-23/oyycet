<script setup lang="ts">
import { NConfigProvider, NMessageProvider, NDialogProvider, NLoadingBarProvider } from 'naive-ui'
import { useTheme } from '@/composables/useTheme'
import AppHeader from './AppHeader.vue'
import AppFooter from './AppFooter.vue'
import BackToTop from './BackToTop.vue'

interface Props {
  maxWidth?: string
}

const props = withDefaults(defineProps<Props>(), {
  maxWidth: '1080px'
})

const { theme, themeOverrides } = useTheme()
</script>

<template>
  <NConfigProvider :theme="theme" :theme-overrides="themeOverrides">
    <NLoadingBarProvider>
      <NMessageProvider>
        <NDialogProvider>
          <div class="app-layout">
            <AppHeader />
            <main class="app-main">
              <div class="app-main-inner" :style="{ maxWidth: props.maxWidth }">
                <slot />
              </div>
            </main>
            <AppFooter />
            <BackToTop />
          </div>
        </NDialogProvider>
      </NMessageProvider>
    </NLoadingBarProvider>
  </NConfigProvider>
</template>

<style scoped>
.app-layout {
  min-height: 100vh;
  background-color: var(--color-bg-page);
  display: flex;
  flex-direction: column;
  transition: background-color var(--transition-normal);
}

.app-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: var(--spacing-6) var(--spacing-6) var(--spacing-10);
}

.app-main-inner {
  margin: 0 auto;
  width: 100%;
  flex: 1;
}

@media (max-width: 768px) {
  .app-main {
    padding: var(--spacing-4) var(--spacing-4) var(--spacing-8);
  }
}
</style>
