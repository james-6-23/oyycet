<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { NButton, NTag, NSpace } from 'naive-ui'
import { getPaper } from '../services/papers'
import type { Paper } from '../types/models'
import LoadingState from '../components/LoadingState.vue'
import ErrorState from '../components/ErrorState.vue'

const route = useRoute()
const router = useRouter()
const id = Number(route.params.id)

const loading = ref(false)
const error = ref('')
const paper = ref<Paper | null>(null)

async function loadPaper() {
  try {
    loading.value = true
    error.value = ''
    paper.value = await getPaper(id)
  } catch (e: any) {
    error.value = e?.message || '加载失败'
  } finally {
    loading.value = false
  }
}

function startPractice() {
  router.push(`/paper/${id}/practice`)
}

function goBack() {
  router.push('/')
}

onMounted(loadPaper)
</script>

<template>
  <div class="paper-detail-page">
    <LoadingState v-if="loading" text="正在加载试卷信息..." />

    <ErrorState
      v-else-if="error"
      :message="error"
      @retry="loadPaper"
    />

    <template v-else-if="paper">
      <div class="detail-card">
        <!-- Header with gradient background -->
        <div class="detail-header">
          <div class="header-bg"></div>
          <div class="header-content">
            <div class="detail-icon-wrapper">
              <svg class="detail-icon-svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                <polyline points="14 2 14 8 20 8"/>
                <line x1="16" y1="13" x2="8" y2="13"/>
                <line x1="16" y1="17" x2="8" y2="17"/>
                <polyline points="10 9 9 9 8 9"/>
              </svg>
            </div>
            <h1 class="detail-title">{{ paper.title }}</h1>
            <NSpace justify="center" :size="8">
              <NTag type="info" round>{{ paper.year }}年</NTag>
              <NTag type="success" round>第{{ paper.month }}月</NTag>
              <NTag round>第{{ paper.paperNo }}套</NTag>
            </NSpace>
          </div>
        </div>

        <!-- Stats cards -->
        <div class="stats-section">
          <div class="stat-card stat-card--type">
            <div class="stat-icon stat-icon--blue">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/>
                <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/>
              </svg>
            </div>
            <div class="stat-info">
              <span class="stat-label">试卷类型</span>
              <span class="stat-value">{{ paper.type || 'CET-4' }}</span>
            </div>
          </div>

          <div class="stat-card stat-card--difficulty">
            <div class="stat-icon stat-icon--purple">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
              </svg>
            </div>
            <div class="stat-info">
              <span class="stat-label">难度等级</span>
              <span class="stat-value">{{ paper.difficulty || '中等' }}</span>
            </div>
          </div>

          <div class="stat-card stat-card--duration">
            <div class="stat-icon stat-icon--green">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="12" cy="12" r="10"/>
                <polyline points="12 6 12 12 16 14"/>
              </svg>
            </div>
            <div class="stat-info">
              <span class="stat-label">建议时长</span>
              <span class="stat-value">{{ paper.durationMin || 120 }} <span class="stat-unit">分钟</span></span>
            </div>
          </div>
        </div>

        <!-- Action buttons -->
        <div class="detail-actions">
          <NButton size="large" class="action-btn action-btn--back" @click="goBack">
            <template #icon>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <line x1="19" y1="12" x2="5" y2="12"/>
                <polyline points="12 19 5 12 12 5"/>
              </svg>
            </template>
            返回列表
          </NButton>
          <NButton type="primary" size="large" class="action-btn action-btn--start" @click="startPractice">
            <template #icon>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <polygon points="5 3 19 12 5 21 5 3"/>
              </svg>
            </template>
            开始练习
          </NButton>
        </div>
      </div>
    </template>
  </div>
</template>

<style scoped>
.paper-detail-page {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: var(--spacing-8) var(--spacing-4);
  min-height: 60vh;
}

.detail-card {
  width: 100%;
  max-width: 640px;
  background: var(--color-bg-card);
  border-radius: var(--radius-2xl);
  box-shadow: var(--shadow-card);
  overflow: hidden;
}

/* Header with gradient */
.detail-header {
  position: relative;
  padding: var(--spacing-10) var(--spacing-6) var(--spacing-8);
  overflow: hidden;
}

.header-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent-purple) 100%);
  opacity: 0.08;
}

.header-content {
  position: relative;
  text-align: center;
}

.detail-icon-wrapper {
  width: 72px;
  height: 72px;
  margin: 0 auto var(--spacing-5);
  background: linear-gradient(135deg, var(--color-primary), var(--color-accent-purple));
  border-radius: var(--radius-xl);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 24px rgba(91, 141, 239, 0.3);
}

.detail-icon-svg {
  width: 36px;
  height: 36px;
  color: #fff;
}

.detail-title {
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-bold);
  color: var(--color-text-primary);
  margin: 0 0 var(--spacing-4);
  line-height: var(--line-height-tight);
}

/* Stats section */
.stats-section {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--spacing-3);
  padding: 0 var(--spacing-6);
  margin-bottom: var(--spacing-8);
}

.stat-card {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  padding: var(--spacing-4);
  border-radius: var(--radius-lg);
  background: var(--color-bg-card-hover);
  transition: transform var(--transition-fast), box-shadow var(--transition-fast);
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-card-hover);
}

.stat-icon {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-icon svg {
  width: 20px;
  height: 20px;
}

.stat-icon--blue {
  background: rgba(91, 141, 239, 0.12);
  color: var(--color-primary);
}

.stat-icon--purple {
  background: rgba(155, 126, 222, 0.12);
  color: var(--color-accent-purple);
}

.stat-icon--green {
  background: rgba(107, 203, 119, 0.12);
  color: var(--color-accent-green);
}

.stat-info {
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.stat-label {
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
  margin-bottom: 2px;
}

.stat-value {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  color: var(--color-text-primary);
  white-space: nowrap;
}

.stat-unit {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-normal);
  color: var(--color-text-secondary);
}

/* Action buttons */
.detail-actions {
  display: flex;
  gap: var(--spacing-4);
  justify-content: center;
  padding: 0 var(--spacing-6) var(--spacing-8);
}

.action-btn--start {
  box-shadow: var(--shadow-button);
  min-width: 160px;
}

.action-btn--start:hover {
  box-shadow: 0 4px 12px rgba(91, 141, 239, 0.35);
}

@media (max-width: 768px) {
  .stats-section {
    grid-template-columns: 1fr;
  }

  .detail-actions {
    flex-direction: column;
    padding: 0 var(--spacing-4) var(--spacing-6);
  }

  .detail-header {
    padding: var(--spacing-8) var(--spacing-4) var(--spacing-6);
  }
}
</style>
