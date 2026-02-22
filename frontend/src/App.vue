<script setup lang="ts">
import { computed } from 'vue'
import { RouterView, useRoute } from 'vue-router'
import AppLayout from './components/AppLayout.vue'
import LandingLayout from './components/LandingLayout.vue'

const route = useRoute()
const isLandingLayout = computed(() => route.meta.layout === 'landing')
</script>

<template>
  <component :is="isLandingLayout ? LandingLayout : AppLayout">
    <RouterView v-slot="{ Component }">
      <Transition name="fade" mode="out-in">
        <component :is="Component" />
      </Transition>
    </RouterView>
  </component>
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
