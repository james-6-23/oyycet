<template>
  <section class="hero-section">
    <!-- Floating decorative elements -->
    <div class="floating-blob blob-1"></div>
    <div class="floating-blob blob-2"></div>
    <div class="floating-blob blob-3"></div>
    <div class="floating-blob blob-4"></div>

    <div class="hero-content">
      <h1 class="hero-title">
        <span class="title-line">轻松征服</span>
        <span class="title-line title-gradient">四级考试</span>
      </h1>

      <p class="hero-subtitle">
        AI 驱动的智能练习平台，精选历年真题，实时评分与详细解析，让备考更高效。
      </p>

      <div class="hero-actions">
        <RouterLink to="/papers" class="btn-primary">开始练习</RouterLink>
        <a href="#features" class="btn-outline">了解更多</a>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<style scoped>
.hero-section {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  padding-top: 100px;
  background:
    radial-gradient(ellipse at 30% 50%, rgba(0, 153, 255, 0.08) 0%, transparent 60%),
    radial-gradient(ellipse at 70% 50%, rgba(139, 92, 246, 0.06) 0%, transparent 60%),
    var(--landing-bg);
}

.hero-content {
  position: relative;
  z-index: 1;
  max-width: 800px;
  text-align: center;
  padding: 0 24px;
}

.hero-title {
  font-size: var(--landing-font-hero, clamp(40px, 5vw, 56px));
  font-weight: 800;
  line-height: 1.2;
  margin-bottom: 24px;
}

.title-line {
  display: block;
}

.title-gradient {
  background: var(--color-primary-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 18px;
  color: var(--color-text-secondary);
  max-width: 600px;
  margin: 0 auto 40px;
  line-height: 1.7;
}

.hero-actions {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  flex-wrap: wrap;
}

.btn-primary {
  display: inline-block;
  padding: 14px 32px;
  background: var(--landing-navy);
  color: #fff;
  border-radius: 12px;
  font-weight: 600;
  font-size: 16px;
  text-decoration: none;
  transition: opacity 0.2s ease, transform 0.2s ease;
}

.btn-primary:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

.btn-outline {
  display: inline-block;
  padding: 14px 32px;
  background: transparent;
  color: inherit;
  border: 2px solid var(--color-border);
  border-radius: 12px;
  font-weight: 600;
  font-size: 16px;
  text-decoration: none;
  transition: background-color 0.2s ease, transform 0.2s ease;
}

.btn-outline:hover {
  background-color: rgba(0, 0, 0, 0.04);
  transform: translateY(-1px);
}

.floating-blob {
  position: absolute;
  border-radius: 50%;
  will-change: transform;
  pointer-events: none;
}

.blob-1 {
  width: 200px;
  height: 200px;
  background: rgba(0, 153, 255, 0.1);
  top: 15%;
  left: 5%;
  animation: float 8s ease-in-out infinite;
}

.blob-2 {
  width: 120px;
  height: 120px;
  background: rgba(139, 92, 246, 0.12);
  top: 20%;
  right: 8%;
  animation: float 10s ease-in-out infinite;
}

.blob-3 {
  width: 80px;
  height: 80px;
  background: rgba(0, 210, 211, 0.15);
  bottom: 20%;
  left: 12%;
  animation: float 6s ease-in-out infinite;
}

.blob-4 {
  width: 150px;
  height: 150px;
  background: rgba(139, 92, 246, 0.1);
  bottom: 15%;
  right: 5%;
  animation: float 12s ease-in-out infinite;
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(5deg);
  }
}
</style>
