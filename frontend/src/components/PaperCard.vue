<script setup lang="ts">
import { NButton, NTag } from 'naive-ui'
import type { Paper } from '@/types/models'

interface Props {
  paper: Paper
  index?: number
}

const props = withDefaults(defineProps<Props>(), {
  index: 0
})

const emit = defineEmits<{
  click: []
}>()

const typeColorMap: Record<string, string> = {
  'CET-4': 'var(--color-primary)',
  'CET-6': 'var(--color-accent-violet)',
}

const accentColor = typeColorMap[props.paper.type || ''] || 'var(--color-primary)'
</script>

<template>
  <div
    class="paper-card"
    :style="{ '--card-accent': accentColor, animationDelay: `${index * 60}ms` }"
    @click="emit('click')"
  >
    <div class="card-accent-bar" />
    <div class="card-body">
      <div class="card-top">
        <div class="card-type-badge">
          {{ paper.type || 'CET-4' }}
        </div>
        <div v-if="paper.attempts" class="card-attempts">
          已练 {{ paper.attempts }} 次
        </div>
      </div>
      <h3 class="paper-title">{{ paper.title }}</h3>
      <div class="paper-tags">
        <NTag v-if="paper.year" size="small" round :bordered="false" class="tag-year">
          {{ paper.year }}年
        </NTag>
        <NTag v-if="paper.month" size="small" round :bordered="false" class="tag-month">
          {{ paper.month }}月
        </NTag>
        <NTag v-if="paper.difficulty" size="small" round :bordered="false" class="tag-difficulty">
          {{ paper.difficulty }}
        </NTag>
      </div>
      <p class="paper-desc">{{ paper.description || '四级英语练习试卷' }}</p>
      <div class="card-footer">
        <div v-if="paper.durationMin" class="paper-duration">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
          {{ paper.durationMin }} 分钟
        </div>
        <NButton class="paper-btn" type="primary" size="small" round>
          开始练习
          <template #icon>
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/></svg>
          </template>
        </NButton>
      </div>
    </div>
  </div>
</template>

<style scoped>
.paper-card {
  --card-accent: var(--color-primary);
  background: var(--color-bg-card);
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow: var(--shadow-card);
  display: flex;
  flex-direction: column;
  cursor: pointer;
  transition: transform 0.3s cubic-bezier(0.34, 1.56, 0.64, 1),
              box-shadow 0.3s ease;
  border: 1px solid var(--color-border-light);
  position: relative;
  animation: cardFadeIn 0.5s ease both;
}

@keyframes cardFadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.paper-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--shadow-card-hover);
  border-color: var(--card-accent);
}

.paper-card:hover .card-accent-bar {
  height: 5px;
}

.paper-card:hover .paper-btn {
  transform: translateX(2px);
}

.card-accent-bar {
  height: 3px;
  background: var(--card-accent);
  transition: height 0.3s ease;
}

.card-body {
  padding: var(--spacing-5) var(--spacing-6) var(--spacing-6);
  display: flex;
  flex-direction: column;
  flex: 1;
}

.card-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-3);
}

.card-type-badge {
  font-size: 11px;
  font-weight: var(--font-weight-bold);
  color: var(--card-accent);
  background: color-mix(in srgb, var(--card-accent) 10%, transparent);
  padding: 2px 10px;
  border-radius: var(--radius-full);
  letter-spacing: 0.5px;
}

.card-attempts {
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
}

.paper-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text-primary);
  margin: 0 0 var(--spacing-3);
  line-height: var(--line-height-tight);
}

.paper-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: var(--spacing-3);
}

.tag-year {
  background: var(--color-primary-lighter) !important;
  color: var(--color-primary) !important;
}

.tag-month {
  background: var(--color-info-light) !important;
  color: var(--color-info) !important;
}

.tag-difficulty {
  background: var(--color-warning-light) !important;
  color: var(--color-warning) !important;
}

.paper-desc {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  margin: 0;
  flex: 1;
  line-height: var(--line-height-relaxed);
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: var(--spacing-5);
  padding-top: var(--spacing-4);
  border-top: 1px solid var(--color-border-light);
}

.paper-duration {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
}

.paper-btn {
  transition: transform 0.2s ease;
}
</style>
