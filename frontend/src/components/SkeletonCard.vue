<script setup lang="ts">
import { NSkeleton } from 'naive-ui'

interface Props {
  count?: number
}

withDefaults(defineProps<Props>(), {
  count: 6
})
</script>

<template>
  <div class="skeleton-grid">
    <div v-for="i in count" :key="i" class="skeleton-card">
      <NSkeleton :width="80" :height="80" circle style="margin-bottom: 16px" />
      <NSkeleton text :repeat="1" style="width: 60%" />
      <NSkeleton text :repeat="1" style="width: 80%; margin-top: 8px" />
      <NSkeleton :height="36" :width="100" style="margin-top: 24px; border-radius: 12px" />
    </div>
  </div>
</template>

<style scoped>
.skeleton-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: var(--spacing-6);
}

.skeleton-card {
  background: var(--color-bg-card);
  border-radius: var(--radius-xl);
  padding: var(--spacing-8) var(--spacing-6);
  box-shadow: var(--shadow-card);
  display: flex;
  flex-direction: column;
  align-items: center;
}

@media (max-width: 768px) {
  .skeleton-grid {
    grid-template-columns: 1fr;
  }
}
</style>
