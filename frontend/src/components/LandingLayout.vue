<script setup lang="ts">
import { NConfigProvider, NMessageProvider, NDialogProvider, NLoadingBarProvider } from 'naive-ui'
import { useTheme } from '@/composables/useTheme'
import BackToTop from './BackToTop.vue'

const { theme, themeOverrides } = useTheme()
</script>

<template>
  <NConfigProvider :theme="theme" :theme-overrides="themeOverrides">
    <NLoadingBarProvider>
      <NMessageProvider>
        <NDialogProvider>
          <div class="landing-layout">
            <slot />
            <BackToTop />
          </div>
        </NDialogProvider>
      </NMessageProvider>
    </NLoadingBarProvider>
  </NConfigProvider>
</template>

<style scoped>
.landing-layout {
  min-height: 100vh;
  background-color: var(--landing-bg);
  display: flex;
  flex-direction: column;
  transition: background-color var(--transition-normal);
  scroll-behavior: smooth;
}
</style>
