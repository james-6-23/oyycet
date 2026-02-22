<script setup lang="ts">
import { NButton, NEmpty } from 'naive-ui'

interface Props {
  title?: string
  description?: string
  actionText?: string
}

const props = withDefaults(defineProps<Props>(), {
  title: '暂无数据',
  description: ''
})

const emit = defineEmits<{
  action: []
}>()
</script>

<template>
  <div class="empty-state">
    <NEmpty :description="props.title">
      <template #extra>
        <p v-if="description" class="empty-description">{{ description }}</p>
        <NButton v-if="actionText" type="primary" @click="emit('action')">
          {{ actionText }}
        </NButton>
      </template>
    </NEmpty>
  </div>
</template>

<style scoped>
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-12);
}

.empty-description {
  color: var(--color-text-muted);
  font-size: var(--font-size-sm);
  margin-bottom: var(--spacing-4);
  text-align: center;
}
</style>
