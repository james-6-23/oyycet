<script setup lang="ts">
import { useScrollReveal } from '@/composables/useScrollReveal'

const { revealRef, isVisible } = useScrollReveal()
defineExpose({ revealRef })

const testimonials = [
  {
    quote: '用了这个平台两个月，四级从 380 分提升到 520 分！AI 解析功能特别好用，每道题都能理解透彻。',
    author: '李同学',
    role: '大二学生',
    rating: 5,
  },
  {
    quote: '最喜欢实时评分功能，做完题马上就能知道结果。错题归纳帮我找到了薄弱环节，复习更有针对性。',
    author: '王同学',
    role: '大三学生',
    rating: 5,
  },
  {
    quote: '界面简洁好用，真题覆盖全面。断点续练功能让我在通勤路上也能刷题，碎片时间利用起来了。',
    author: '张同学',
    role: '考研备考生',
    rating: 5,
  },
]
</script>

<template>
  <section id="testimonials" class="social-proof">
    <div class="container">
      <h2 class="section-title">学员评价</h2>
      <p class="section-subtitle">听听他们怎么说</p>

      <div
        ref="revealRef"
        class="testimonial-grid"
        :class="{ revealed: isVisible }"
      >
        <div
          v-for="(item, index) in testimonials"
          :key="index"
          class="testimonial-card"
          :style="{ transitionDelay: `${index * 100}ms` }"
        >
          <div class="quote-mark">"</div>
          <div class="stars">
            <span v-for="s in item.rating" :key="s" class="star">★</span>
          </div>
          <p class="quote-text">{{ item.quote }}</p>
          <div class="author-info">
            <div class="author-avatar">{{ item.author.charAt(0) }}</div>
            <div>
              <div class="author-name">{{ item.author }}</div>
              <div class="author-role">{{ item.role }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.social-proof {
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
/* PLACEHOLDER_STYLES */

.testimonial-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
  text-align: left;
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 0.6s ease, transform 0.6s ease;
}

.testimonial-grid.revealed {
  opacity: 1;
  transform: translateY(0);
}

.testimonial-card {
  background: var(--landing-glass-bg);
  backdrop-filter: blur(var(--landing-blur-glass));
  border: 1px solid var(--landing-glass-border);
  border-radius: var(--landing-radius-card);
  box-shadow: var(--landing-shadow-card);
  padding: 32px;
  position: relative;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  opacity: 0;
  transform: translateY(20px);
}

.testimonial-grid.revealed .testimonial-card {
  opacity: 1;
  transform: translateY(0);
  transition: transform 0.3s ease, box-shadow 0.3s ease, opacity 0.5s ease;
}

.testimonial-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px rgba(0, 67, 112, 0.15);
}

.quote-mark {
  font-size: 48px;
  font-weight: 800;
  color: var(--landing-cyan);
  opacity: 0.3;
  line-height: 1;
  margin-bottom: 8px;
}

.stars {
  display: flex;
  gap: 2px;
  margin-bottom: 16px;
}

.star {
  color: #F59E0B;
  font-size: 16px;
}

.quote-text {
  font-size: 15px;
  color: var(--color-text-secondary);
  line-height: 1.7;
  margin: 0 0 24px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--color-primary-gradient);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 14px;
  flex-shrink: 0;
}

.author-name {
  font-weight: 600;
  font-size: 14px;
  color: var(--color-text-primary);
}

.author-role {
  font-size: 13px;
  color: var(--color-text-muted);
  margin-top: 2px;
}

@media (max-width: 768px) {
  .social-proof {
    padding: 60px 0;
  }

  .container {
    padding: 0 16px;
  }

  .testimonial-grid {
    grid-template-columns: 1fr;
  }
}
</style>
