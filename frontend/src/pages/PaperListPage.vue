<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getPapers } from '../services/papers'
import type { Paper } from '../types/models'
import PaperCard from '../components/PaperCard.vue'
import SkeletonCard from '../components/SkeletonCard.vue'
import EmptyState from '../components/EmptyState.vue'
import ErrorState from '../components/ErrorState.vue'

const router = useRouter()
const loading = ref(false)
const error = ref('')
const papers = ref<Paper[]>([])

const paperCount = computed(() => papers.value.length)

async function loadPapers() {
  try {
    loading.value = true
    error.value = ''
    const page = await getPapers({ current: 1, size: 20 })
    papers.value = page.records || []
  } catch (e: any) {
    error.value = e?.message || '加载失败'
  } finally {
    loading.value = false
  }
}

function goToPaper(id: number) {
  router.push(`/paper/${id}`)
}

onMounted(loadPapers)
</script>

<template>
  <div class="paper-list-page">
    <!-- Hero Section -->
    <header class="hero-section">
      <div class="hero-bg-shapes">
        <div class="shape shape-1" />
        <div class="shape shape-2" />
        <div class="shape shape-3" />
      </div>
      <div class="hero-content">
        <div class="hero-badge">
          <span class="badge-dot" />
          <span>CET-4 真题练习平台</span>
        </div>
        <h1 class="hero-title">
          备考、练习、<span class="hero-title-accent">通关</span>。
        </h1>
        <p class="hero-subtitle">
          精选历年四级真题，智能评分与解析，助你高效备考、轻松过级。
        </p>
        <div v-if="!loading && papers.length > 0" class="hero-stats">
          <div class="stat-item">
            <span class="stat-number">{{ paperCount }}</span>
            <span class="stat-label">套试卷</span>
          </div>
          <div class="stat-divider" />
          <div class="stat-item">
            <span class="stat-number">4</span>
            <span class="stat-label">大题型</span>
          </div>
          <div class="stat-divider" />
          <div class="stat-item">
            <span class="stat-number">AI</span>
            <span class="stat-label">智能解析</span>
          </div>
        </div>
      </div>
    </header>

    <!-- Paper Grid Section -->
    <section class="papers-section">
      <div class="section-header">
        <h2 class="section-title">全部试卷</h2>
        <span v-if="!loading && papers.length > 0" class="section-count">
          共 {{ paperCount }} 套
        </span>
      </div>

      <SkeletonCard v-if="loading" :count="6" />

      <ErrorState
        v-else-if="error"
        :message="error"
        @retry="loadPapers"
      />

      <EmptyState
        v-else-if="papers.length === 0"
        title="暂无试卷"
        description="还没有可用的试卷，请稍后再来"
      />

      <div v-else class="paper-grid">
        <PaperCard
          v-for="(paper, idx) in papers"
          :key="paper.id"
          :paper="paper"
          :index="idx"
          @click="goToPaper(paper.id)"
        />
      </div>
    </section>
  </div>
</template>

<style scoped>
.paper-list-page {
  padding: 0;
}

/* ===== Hero Section ===== */
.hero-section {
  position: relative;
  text-align: center;
  padding: var(--spacing-12) var(--spacing-6) var(--spacing-10);
  margin: calc(-1 * var(--spacing-6)) calc(-1 * var(--spacing-6)) 0;
  background: linear-gradient(
    160deg,
    var(--color-primary-lighter) 0%,
    var(--color-bg-page) 50%,
    #F0EBFF 100%
  );
  overflow: hidden;
  border-radius: 0 0 var(--radius-2xl) var(--radius-2xl);
}

.hero-bg-shapes {
  position: absolute;
  inset: 0;
  pointer-events: none;
  overflow: hidden;
}

.shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.15;
}

.shape-1 {
  width: 300px;
  height: 300px;
  background: var(--color-primary);
  top: -80px;
  right: -60px;
  animation: floatShape 8s ease-in-out infinite;
}

.shape-2 {
  width: 200px;
  height: 200px;
  background: var(--color-accent-violet);
  bottom: -40px;
  left: -40px;
  animation: floatShape 10s ease-in-out infinite reverse;
}

.shape-3 {
  width: 120px;
  height: 120px;
  background: var(--color-accent-cyan);
  top: 40%;
  left: 60%;
  animation: floatShape 12s ease-in-out infinite 2s;
}

@keyframes floatShape {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(15px, -15px) scale(1.05); }
}

.hero-content {
  position: relative;
  z-index: 1;
  max-width: 600px;
  margin: 0 auto;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 16px;
  background: var(--color-bg-elevated);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-full);
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
  color: var(--color-primary);
  margin-bottom: var(--spacing-5);
  box-shadow: 0 2px 8px rgba(79, 110, 247, 0.08);
}

.badge-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--color-primary);
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; transform: scale(1); }
  50% { opacity: 0.5; transform: scale(0.8); }
}

.hero-title {
  font-size: clamp(28px, 5vw, 40px);
  font-weight: 800;
  color: var(--color-text-primary);
  margin: 0 0 var(--spacing-3);
  line-height: 1.2;
  letter-spacing: -0.5px;
}

.hero-title-accent {
  background: var(--color-primary-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: var(--font-size-base);
  color: var(--color-text-secondary);
  margin: 0 0 var(--spacing-6);
  line-height: var(--line-height-relaxed);
  max-width: 480px;
  margin-left: auto;
  margin-right: auto;
}

.hero-stats {
  display: inline-flex;
  align-items: center;
  gap: var(--spacing-5);
  padding: var(--spacing-3) var(--spacing-6);
  background: var(--color-bg-elevated);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-card);
  border: 1px solid var(--color-border-light);
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}

.stat-number {
  font-size: var(--font-size-xl);
  font-weight: 800;
  color: var(--color-primary);
  line-height: 1;
}

.stat-label {
  font-size: 11px;
  color: var(--color-text-muted);
  font-weight: var(--font-weight-medium);
}

.stat-divider {
  width: 1px;
  height: 28px;
  background: var(--color-border);
}

/* ===== Papers Section ===== */
.papers-section {
  padding: var(--spacing-8) 0 var(--spacing-4);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-6);
}

.section-title {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--color-text-primary);
  margin: 0;
}

.section-count {
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
  background: var(--color-bg-subtle);
  padding: 4px 12px;
  border-radius: var(--radius-full);
}

.paper-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: var(--spacing-6);
}

@media (max-width: 768px) {
  .hero-section {
    padding: var(--spacing-8) var(--spacing-4) var(--spacing-8);
    margin: calc(-1 * var(--spacing-4)) calc(-1 * var(--spacing-4)) 0;
  }

  .hero-stats {
    gap: var(--spacing-3);
    padding: var(--spacing-3) var(--spacing-4);
  }

  .paper-grid {
    grid-template-columns: 1fr;
  }
}
</style>
