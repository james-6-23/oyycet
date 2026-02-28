<script setup lang="ts">
import { useScrollReveal } from '@/composables/useScrollReveal'
import { NIcon } from 'naive-ui'
import {
  DocumentTextOutline,
  SparklesOutline,
  FlashOutline,
  BarChartOutline
} from '@vicons/ionicons5'

const { revealRef, isVisible } = useScrollReveal()
defineExpose({ revealRef })

interface FeatureCard {
  icon: any
  title: string
  desc: string
}

const features: FeatureCard[] = [
  {
    icon: DocumentTextOutline,
    title: '历年真题',
    desc: '收录完整的 CET-4 历年真题，题型全面覆盖听力、阅读、翻译和写作。',
  },
  {
    icon: SparklesOutline,
    title: 'AI 智能解析',
    desc: '基于 AI 的智能解析系统，为每道题提供详细的解题思路和知识点讲解。',
  },
  {
    icon: FlashOutline,
    title: '实时评分',
    desc: '提交即出分，精准评估你的答题表现，快速了解薄弱环节。',
  },
  {
    icon: BarChartOutline,
    title: '进度追踪',
    desc: '完整的练习记录和数据分析，可视化你的备考进度和成绩趋势。',
  },
]
</script>

<template>
  <section id="features" class="features-section">
    <div class="container">
      <h2 class="section-title">核心功能</h2>
      <p class="section-subtitle">全方位助力你的四级备考之旅</p>

      <div
        ref="revealRef"
        class="card-grid"
        :class="{ revealed: isVisible }"
      >
        <div
          v-for="(feature, index) in features"
          :key="index"
          class="feature-card"
          :style="{ transitionDelay: `${index * 100}ms` }"
        >
          <div class="icon-circle">
            <n-icon size="24" :component="feature.icon" />
          </div>
          <h3 class="card-title">{{ feature.title }}</h3>
          <p class="card-desc">{{ feature.desc }}</p>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.features-section {
  padding: var(--landing-section-padding) 0;
}

.container {
  max-width: var(--landing-content-max);
  margin: 0 auto;
  padding: 0 24px;
  text-align: center;
}

.section-title {
  font-size: var(--landing-font-section);
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0 0 12px;
}

.section-subtitle {
  font-size: 16px;
  color: var(--color-text-secondary);
  margin: 0 0 48px;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
  text-align: left;
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 0.6s ease, transform 0.6s ease;
}

.card-grid.revealed {
  opacity: 1;
  transform: translateY(0);
}

.feature-card {
  background: var(--landing-glass-bg);
  backdrop-filter: blur(var(--landing-blur-glass));
  border: 1px solid var(--landing-glass-border);
  border-radius: var(--landing-radius-card);
  box-shadow: var(--landing-shadow-card);
  padding: 32px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  opacity: 0;
  transform: translateY(20px);
}

.card-grid.revealed .feature-card {
  opacity: 1;
  transform: translateY(0);
  transition: transform 0.3s ease, box-shadow 0.3s ease, opacity 0.5s ease;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px rgba(0, 67, 112, 0.15);
}

.icon-circle {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: var(--landing-glass-bg, rgba(255, 255, 255, 0.15));
  border: 1px solid var(--landing-glass-border);
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon {
  font-size: 22px;
  line-height: 1;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 20px 0 0;
}

.card-desc {
  font-size: 14px;
  color: var(--color-text-secondary);
  line-height: 1.6;
  margin: 12px 0 0;
}

@media (max-width: 768px) {
  .features-section {
    padding: 60px 0;
  }

  .container {
    padding: 0 16px;
  }

  .card-grid {
    grid-template-columns: 1fr;
  }
}
</style>