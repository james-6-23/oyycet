<script setup lang="ts">
import { useScrollReveal } from '@/composables/useScrollReveal'

const { revealRef: item1Ref, isVisible: item1Visible } = useScrollReveal()
const { revealRef: item2Ref, isVisible: item2Visible } = useScrollReveal()
defineExpose({ item1Ref, item2Ref })

const showcaseItems = [
  {
    title: '智能练习系统',
    description: '我们的练习系统涵盖 CET-4 所有题型，提供真实的考试体验。',
    bullets: [
      '听力、阅读、翻译、写作四大题型全覆盖',
      '模拟真实考试计时，培养时间管理能力',
      '支持断点续练，随时随地灵活学习',
    ],
    visualLabel: '练习界面预览',
  },
  {
    title: '详细解析与评分',
    description: '每道题都配有 AI 生成的详细解析，帮助你理解答题思路。',
    bullets: [
      'AI 驱动的智能评分，精准评估答题质量',
      '逐题详细解析，深入理解知识点',
      '错题自动归纳，针对性强化训练',
    ],
    visualLabel: '解析界面预览',
  },
]
</script>

<template>
  <section id="product" class="product-showcase">
    <div class="container">
      <h2 class="section-title">产品展示</h2>

      <div class="showcase-items">
        <!-- Item 1: text left, visual right -->
        <div
          ref="item1Ref"
          class="showcase-item"
          :class="{ revealed: item1Visible }"
        >
          <div class="showcase-text">
            <h3 class="showcase-item-title">{{ showcaseItems[0].title }}</h3>
            <p class="showcase-description">{{ showcaseItems[0].description }}</p>
            <ul class="showcase-bullets">
              <li v-for="(bullet, idx) in showcaseItems[0].bullets" :key="idx">
                <span class="bullet-icon">&#10003;</span>
                <span>{{ bullet }}</span>
              </li>
            </ul>
          </div>
          <div class="showcase-visual">
            <div class="visual-placeholder visual-placeholder--1">
              <span class="visual-label">{{ showcaseItems[0].visualLabel }}</span>
            </div>
          </div>
        </div>

        <!-- Item 2: visual left, text right (reversed) -->
        <div
          ref="item2Ref"
          class="showcase-item showcase-item--reversed"
          :class="{ revealed: item2Visible }"
        >
          <div class="showcase-text">
            <h3 class="showcase-item-title">{{ showcaseItems[1].title }}</h3>
            <p class="showcase-description">{{ showcaseItems[1].description }}</p>
            <ul class="showcase-bullets">
              <li v-for="(bullet, idx) in showcaseItems[1].bullets" :key="idx">
                <span class="bullet-icon">&#10003;</span>
                <span>{{ bullet }}</span>
              </li>
            </ul>
          </div>
          <div class="showcase-visual">
            <div class="visual-placeholder visual-placeholder--2">
              <span class="visual-label">{{ showcaseItems[1].visualLabel }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.product-showcase {
  padding: var(--landing-section-padding) 0;
}

.container {
  max-width: var(--landing-content-max);
  margin: 0 auto;
  padding: 0 24px;
}

.section-title {
  font-size: var(--landing-font-section);
  font-weight: 700;
  color: var(--color-text-primary);
  text-align: center;
  margin: 0 0 60px;
}

.showcase-items {
  display: flex;
  flex-direction: column;
  gap: 80px;
}

.showcase-item {
  display: flex;
  gap: 60px;
  align-items: center;
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 0.6s ease, transform 0.6s ease;
}

.showcase-item.revealed {
  opacity: 1;
  transform: translateY(0);
}

.showcase-item--reversed {
  flex-direction: row-reverse;
}
.showcase-text {
  flex: 1;
}

.showcase-visual {
  flex: 1;
  min-width: 0;
}

.showcase-item-title {
  font-size: 24px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0 0 16px;
}

.showcase-description {
  font-size: 16px;
  color: var(--color-text-secondary);
  line-height: 1.7;
  margin: 0 0 24px;
}

.showcase-bullets {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.showcase-bullets li {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  font-size: 15px;
  color: var(--color-text-secondary);
  line-height: 1.6;
}

.bullet-icon {
  color: var(--color-primary);
  font-weight: 700;
  font-size: 14px;
  flex-shrink: 0;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 2px;
}

.visual-placeholder {
  height: 320px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
}

.visual-placeholder--1 {
  background: linear-gradient(135deg, var(--color-primary-lighter) 0%, rgba(0, 153, 255, 0.1) 100%);
}

.visual-placeholder--2 {
  background: linear-gradient(135deg, rgba(0, 153, 255, 0.1) 0%, var(--color-primary-lighter) 100%);
}

.visual-label {
  font-size: 16px;
  font-weight: 500;
  color: var(--color-text-muted);
  opacity: 0.7;
}

@media (max-width: 768px) {
  .product-showcase {
    padding: 60px 0;
  }

  .container {
    padding: 0 16px;
  }

  .section-title {
    margin-bottom: 40px;
  }

  .showcase-items {
    gap: 60px;
  }

  .showcase-item,
  .showcase-item--reversed {
    flex-direction: column;
    gap: 32px;
  }

  .showcase-visual {
    width: 100%;
  }

  .visual-placeholder {
    height: 240px;
  }
}
</style>
