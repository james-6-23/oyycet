<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { NTag } from 'naive-ui'
import { getMyRecords, getMyStats } from '../services/records'
import type { PracticeRecord, PracticeStats } from '../types/models'
import LoadingState from '../components/LoadingState.vue'
import EmptyState from '../components/EmptyState.vue'
import ErrorState from '../components/ErrorState.vue'

const router = useRouter()
const loading = ref(false)
const error = ref('')
const records = ref<PracticeRecord[]>([])
const stats = ref<PracticeStats | null>(null)

async function loadRecords() {
  try {
    loading.value = true
    error.value = ''
    const [page, st] = await Promise.all([
      getMyRecords({ current: 1, size: 20 }),
      getMyStats()
    ])
    records.value = page.records || []
    stats.value = st
  } catch (e: any) {
    error.value = e?.message || '加载失败（需要先登录）'
  } finally {
    loading.value = false
  }
}

function getScoreLevel(score: number) {
  if (score >= 90) return { type: 'success' as const, label: '优秀', color: 'var(--color-success)' }
  if (score >= 70) return { type: 'info' as const, label: '良好', color: 'var(--color-primary)' }
  if (score >= 60) return { type: 'warning' as const, label: '及格', color: 'var(--color-warning)' }
  return { type: 'error' as const, label: '需努力', color: 'var(--color-error)' }
}

function formatDate(dateStr: string) {
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

function goToPractice() {
  router.push('/')
}

onMounted(loadRecords)
</script>

<template>
  <div class="records-page">
    <header class="page-header">
      <div class="page-header-icon">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <line x1="18" y1="20" x2="18" y2="10"/>
          <line x1="12" y1="20" x2="12" y2="4"/>
          <line x1="6" y1="20" x2="6" y2="14"/>
        </svg>
      </div>
      <h1 class="page-title">我的记录</h1>
      <p class="page-subtitle">查看你的学习进度和练习成绩</p>
    </header>

    <LoadingState v-if="loading" text="正在加载记录..." />

    <ErrorState
      v-else-if="error"
      :message="error"
      @retry="loadRecords"
    />

    <template v-else>
      <!-- Stats cards -->
      <div v-if="stats" class="stats-grid">
        <div class="stat-card stat-card--practices">
          <div class="stat-card-icon stat-card-icon--blue">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
              <polyline points="14 2 14 8 20 8"/>
              <line x1="16" y1="13" x2="8" y2="13"/>
              <line x1="16" y1="17" x2="8" y2="17"/>
            </svg>
          </div>
          <div class="stat-card-body">
            <span class="stat-card-label">练习次数</span>
            <span class="stat-card-value">{{ stats.totalPractices }}<span class="stat-card-suffix">次</span></span>
          </div>
        </div>

        <div class="stat-card stat-card--score">
          <div class="stat-card-icon stat-card-icon--purple">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/>
              <polyline points="17 6 23 6 23 12"/>
            </svg>
          </div>
          <div class="stat-card-body">
            <span class="stat-card-label">平均分数</span>
            <span class="stat-card-value">{{ stats.averageScore }}<span class="stat-card-suffix">分</span></span>
          </div>
        </div>

        <div class="stat-card stat-card--accuracy">
          <div class="stat-card-icon stat-card-icon--green">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <circle cx="12" cy="12" r="10"/>
              <circle cx="12" cy="12" r="6"/>
              <circle cx="12" cy="12" r="2"/>
            </svg>
          </div>
          <div class="stat-card-body">
            <span class="stat-card-label">正确率</span>
            <span class="stat-card-value">{{ stats.accuracy }}<span class="stat-card-suffix">%</span></span>
          </div>
        </div>
      </div>

      <!-- Record list -->
      <EmptyState
        v-if="records.length === 0"
        title="暂无练习记录"
        description="开始你的第一次练习吧"
        action-text="去练习"
        @action="goToPractice"
      />

      <div v-else class="records-list">
        <div
          v-for="record in records"
          :key="record.id"
          class="record-card"
        >
          <div class="record-content">
            <div class="record-info">
              <h3 class="record-title">{{ record.paperTitle || `试卷 #${record.paperId}` }}</h3>
              <p class="record-date">
                <svg class="record-date-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                  <line x1="16" y1="2" x2="16" y2="6"/>
                  <line x1="8" y1="2" x2="8" y2="6"/>
                  <line x1="3" y1="10" x2="21" y2="10"/>
                </svg>
                {{ formatDate(record.finishTime) }}
              </p>
            </div>
            <div class="record-score-area">
              <span class="score-value" :style="{ color: getScoreLevel(record.score).color }">{{ record.score }}</span>
              <span class="score-label">分</span>
              <NTag :type="getScoreLevel(record.score).type" size="small" round>
                {{ getScoreLevel(record.score).label }}
              </NTag>
            </div>
          </div>
          <div class="record-detail">
            <div class="record-detail-bar">
              <div
                class="record-detail-bar-fill"
                :style="{ width: (record.correctCount / record.totalCount * 100) + '%' }"
              ></div>
            </div>
            <span class="record-detail-text">
              正确 {{ record.correctCount }} / {{ record.totalCount }} 题
            </span>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<style scoped>
.records-page {
  padding: var(--spacing-6) var(--spacing-4);
  max-width: 800px;
  margin: 0 auto;
}

/* Page header */
.page-header {
  text-align: center;
  margin-bottom: var(--spacing-8);
}

.page-header-icon {
  width: 56px;
  height: 56px;
  margin: 0 auto var(--spacing-4);
  background: linear-gradient(135deg, var(--color-primary), var(--color-accent-purple));
  border-radius: var(--radius-xl);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 6px 20px rgba(91, 141, 239, 0.25);
}

.page-header-icon svg {
  width: 28px;
  height: 28px;
  color: #fff;
}

.page-title {
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-bold);
  color: var(--color-text-primary);
  margin: 0 0 var(--spacing-2);
}

.page-subtitle {
  font-size: var(--font-size-base);
  color: var(--color-text-secondary);
  margin: 0;
}

/* Stats grid */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--spacing-4);
  margin-bottom: var(--spacing-8);
}

.stat-card {
  background: var(--color-bg-card);
  border-radius: var(--radius-xl);
  padding: var(--spacing-5);
  box-shadow: var(--shadow-card);
  display: flex;
  align-items: center;
  gap: var(--spacing-4);
  transition: transform var(--transition-fast), box-shadow var(--transition-fast);
}

.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: var(--shadow-card-hover);
}

.stat-card-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-card-icon svg {
  width: 24px;
  height: 24px;
}

.stat-card-icon--blue {
  background: rgba(91, 141, 239, 0.12);
  color: var(--color-primary);
}

.stat-card-icon--purple {
  background: rgba(155, 126, 222, 0.12);
  color: var(--color-accent-purple);
}

.stat-card-icon--green {
  background: rgba(107, 203, 119, 0.12);
  color: var(--color-accent-green);
}

.stat-card-body {
  display: flex;
  flex-direction: column;
}

.stat-card-label {
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
  margin-bottom: 2px;
}

.stat-card-value {
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-bold);
  color: var(--color-text-primary);
  line-height: var(--line-height-tight);
}

.stat-card-suffix {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-normal);
  color: var(--color-text-secondary);
  margin-left: 2px;
}

/* Records list */
.records-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-4);
}

.record-card {
  background: var(--color-bg-card);
  border-radius: var(--radius-xl);
  padding: var(--spacing-5);
  box-shadow: var(--shadow-card);
  transition: transform var(--transition-fast), box-shadow var(--transition-fast);
  border: 1px solid transparent;
}

.record-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-card-hover);
  border-color: rgba(91, 141, 239, 0.15);
}

.record-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: var(--spacing-4);
}

.record-info {
  flex: 1;
  min-width: 0;
}

.record-title {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text-primary);
  margin: 0 0 var(--spacing-2);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.record-date {
  display: flex;
  align-items: center;
  gap: var(--spacing-1);
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
  margin: 0;
}

.record-date-icon {
  width: 14px;
  height: 14px;
  flex-shrink: 0;
}

.record-score-area {
  display: flex;
  align-items: baseline;
  gap: var(--spacing-2);
  flex-shrink: 0;
}

.score-value {
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-bold);
}

.score-label {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

/* Record detail with progress bar */
.record-detail {
  margin-top: var(--spacing-4);
  padding-top: var(--spacing-3);
  border-top: 1px solid var(--color-border-light);
}

.record-detail-bar {
  height: 6px;
  background: var(--color-border-light);
  border-radius: var(--radius-full);
  overflow: hidden;
  margin-bottom: var(--spacing-2);
}

.record-detail-bar-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--color-primary), var(--color-accent-green));
  border-radius: var(--radius-full);
  transition: width var(--transition-slow);
}

.record-detail-text {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .stat-card {
    padding: var(--spacing-4);
  }

  .record-content {
    flex-direction: column;
    gap: var(--spacing-3);
  }

  .record-score-area {
    align-self: flex-end;
  }
}
</style>
