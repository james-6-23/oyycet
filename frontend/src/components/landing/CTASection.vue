<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<template>
  <section class="cta-section">
    <div class="cta-bg-overlay" />
    <div class="container">
      <h2 class="cta-title">准备好开始了吗？</h2>
      <p class="cta-subtitle">
        加入我们，开启高效的四级备考之旅。<br />
        完全免费，无需注册即可体验。
      </p>
      <RouterLink to="/papers" class="cta-button">
        立即开始练习
      </RouterLink>
      <p class="cta-note">免费使用 · 无需信用卡</p>
    </div>
  </section>
</template>

<style scoped>
.cta-section {
  position: relative;
  padding: var(--landing-section-padding) 0;
  background: var(--landing-navy);
  overflow: hidden;
}

.cta-bg-overlay {
  position: absolute;
  inset: 0;
  background: radial-gradient(ellipse at 50% 0%, rgba(0, 153, 255, 0.15) 0%, transparent 70%);
  pointer-events: none;
}

.container {
  position: relative;
  max-width: var(--landing-content-max);
  margin: 0 auto;
  padding: 0 24px;
  text-align: center;
}

.cta-title {
  font-size: var(--landing-font-section);
  font-weight: 700;
  color: #ffffff;
  margin: 0 0 16px;
}

.cta-subtitle {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.75);
  line-height: 1.7;
  margin: 0 0 40px;
  max-width: 500px;
  margin-left: auto;
  margin-right: auto;
}

.cta-button {
  display: inline-block;
  padding: 16px 40px;
  background: #ffffff;
  color: var(--landing-navy);
  font-size: 16px;
  font-weight: 600;
  border-radius: 12px;
  text-decoration: none;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.cta-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}

.cta-note {
  margin: 20px 0 0;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.5);
}

@media (max-width: 768px) {
  .cta-section {
    padding: 60px 0;
  }

  .container {
    padding: 0 16px;
  }

  .cta-subtitle {
    font-size: 16px;
  }
}
</style>
