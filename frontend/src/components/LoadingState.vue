<script setup lang="ts">
import { NSpin } from 'naive-ui'

interface Props {
  text?: string
  size?: 'small' | 'medium' | 'large'
}

withDefaults(defineProps<Props>(), {
  text: '加载中...',
  size: 'medium'
})
</script>

<template>
  <div class="loading-state">
    <NSpin :size="size" />
    <p v-if="text" class="loading-text">{{ text }}</p>
  </div>
</template>

<style scoped>
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-12);
  gap: var(--spacing-4);
}

.loading-text {
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
}
</style>
