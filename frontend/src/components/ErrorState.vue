<script setup lang="ts">
import { NButton, NResult } from 'naive-ui'

interface Props {
  title?: string
  message?: string
  retryText?: string
}

const props = withDefaults(defineProps<Props>(), {
  title: '出错了',
  message: '加载失败，请稍后重试',
  retryText: '重试'
})

const emit = defineEmits<{
  retry: []
}>()
</script>

<template>
  <div class="error-state">
    <NResult status="error" :title="props.title" :description="props.message">
      <template #footer>
        <NButton type="primary" @click="emit('retry')">
          {{ retryText }}
        </NButton>
      </template>
    </NResult>
  </div>
</template>

<style scoped>
.error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-12);
}
</style>
