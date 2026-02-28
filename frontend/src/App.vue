<script setup lang="ts">
import { computed } from 'vue'
import { RouterView, useRoute } from 'vue-router'
import { NConfigProvider, NMessageProvider, NDialogProvider, NLoadingBarProvider } from 'naive-ui'
import { useTheme } from '@/composables/useTheme'
import AppLayout from './components/AppLayout.vue'
import LandingLayout from './components/LandingLayout.vue'

const route = useRoute()
const { theme, themeOverrides } = useTheme()

const isLandingLayout = computed(() => route.meta.layout === 'landing')
const isAdminLayout = computed(() =>
  route.meta.requiresAdmin || route.matched.some((r) => r.meta.requiresAdmin),
)
</script>

<template>
  <template v-if="isAdminLayout">
    <NConfigProvider :theme="theme" :theme-overrides="themeOverrides">
      <NLoadingBarProvider>
        <NMessageProvider>
          <NDialogProvider>
            <RouterView v-slot="{ Component }">
              <component :is="Component" />
            </RouterView>
          </NDialogProvider>
        </NMessageProvider>
      </NLoadingBarProvider>
    </NConfigProvider>
  </template>
  <template v-else>
    <component :is="isLandingLayout ? LandingLayout : AppLayout">
      <RouterView v-slot="{ Component }">
        <Transition name="fade" mode="out-in">
          <component :is="Component" />
        </Transition>
      </RouterView>
    </component>
  </template>
</template>

<style>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
