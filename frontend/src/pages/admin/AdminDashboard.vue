<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { NCard, NStatistic, NSpin, NGrid, NGi } from 'naive-ui'
import type { AdminDashboardStats } from '@/types/models'
import { getAdminDashboardStats } from '@/services/admin'

const loading = ref(true)
const stats = ref<AdminDashboardStats | null>(null)
const error = ref('')

async function loadStats() {
  loading.value = true
  error.value = ''
  try {
    stats.value = await getAdminDashboardStats()
  } catch (e: any) {
    error.value = e.message || '加载失败'
  } finally {
    loading.value = false
  }
}

onMounted(loadStats)
</script>

<template>
  <div class="dashboard-page">
    <h2 class="page-title">仪表盘</h2>
    <NSpin :show="loading">
      <div v-if="error" class="error-text">{{ error }}</div>
      <NGrid v-else-if="stats" :x-gap="16" :y-gap="16" cols="1 s:2 m:3 l:5" responsive="screen">
        <NGi>
          <NCard class="stat-card">
            <NStatistic label="用户总数" :value="stats.totalUsers" />
          </NCard>
        </NGi>
        <NGi>
          <NCard class="stat-card">
            <NStatistic label="试卷总数" :value="stats.totalPapers" />
          </NCard>
        </NGi>
        <NGi>
          <NCard class="stat-card">
            <NStatistic label="已发布" :value="stats.totalPublished" />
          </NCard>
        </NGi>
        <NGi>
          <NCard class="stat-card">
            <NStatistic label="练习总数" :value="stats.totalRecords" />
          </NCard>
        </NGi>
        <NGi>
          <NCard class="stat-card">
            <NStatistic label="今日练习" :value="stats.todayRecords" />
          </NCard>
        </NGi>
      </NGrid>
    </NSpin>
  </div>
</template>

<style scoped>
.dashboard-page {
  max-width: 1200px;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 20px;
  color: var(--color-text-primary, #1a1a1a);
}

.stat-card {
  text-align: center;
}

.error-text {
  color: #d03050;
  padding: 20px;
}
</style>
