<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, RouterLink } from 'vue-router'
import { NForm, NFormItem, NInput, NButton, useMessage } from 'naive-ui'
import type { FormInst, FormRules } from 'naive-ui'
import { login } from '../services/auth'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const auth = useAuthStore()
const message = useMessage()

const formRef = ref<FormInst | null>(null)
const formValue = ref({
  username: '',
  password: ''
})
const loading = ref(false)

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

async function onSubmit() {
  try {
    await formRef.value?.validate()
    loading.value = true
    const resp = await login({
      username: formValue.value.username,
      password: formValue.value.password
    })
    auth.setAuth(resp.token, resp.user)
    message.success('登录成功！')
    await router.push('/')
  } catch (e: any) {
    if (e?.message) {
      message.error(e.message)
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-page">
    <!-- Floating blobs matching hero section -->
    <div class="floating-blob blob-1"></div>
    <div class="floating-blob blob-2"></div>
    <div class="floating-blob blob-3"></div>
    <div class="floating-blob blob-4"></div>

    <div class="auth-container">
      <!-- Left: Branding -->
      <div class="auth-branding">
        <div class="brand-row">
          <div class="brand-logo">C4</div>
          <span class="brand-text">CET-4 Practice</span>
        </div>
        <h1 class="branding-title">
          <span class="title-line">欢迎回来</span>
          <span class="title-line title-gradient">继续学习之旅</span>
        </h1>
        <p class="branding-desc">
          AI 驱动的智能练习平台，精选历年真题，助你轻松征服四级考试。
        </p>

        <!-- Stats cards -->
        <div class="stats-row">
          <div class="stat-card">
            <span class="stat-icon">📝</span>
            <div class="stat-info">
              <span class="stat-value">500+</span>
              <span class="stat-label">历年真题</span>
            </div>
          </div>
          <div class="stat-card">
            <span class="stat-icon">🤖</span>
            <div class="stat-info">
              <span class="stat-value">AI</span>
              <span class="stat-label">智能解析</span>
            </div>
          </div>
          <div class="stat-card">
            <span class="stat-icon">⚡</span>
            <div class="stat-info">
              <span class="stat-value">实时</span>
              <span class="stat-label">评分系统</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Right: Form Card -->
      <div class="auth-form-wrapper">
        <div class="auth-card">
          <div class="card-header">
            <h2 class="card-title">登录账号</h2>
            <p class="card-subtitle">输入你的账号信息以继续</p>
          </div>

          <NForm ref="formRef" :model="formValue" :rules="rules" label-placement="top">
            <NFormItem label="用户名" path="username">
              <NInput
                v-model:value="formValue.username"
                placeholder="请输入用户名"
                size="large"
                @keyup.enter="onSubmit"
              />
            </NFormItem>

            <NFormItem label="密码" path="password">
              <NInput
                v-model:value="formValue.password"
                type="password"
                placeholder="请输入密码"
                size="large"
                show-password-on="click"
                @keyup.enter="onSubmit"
              />
            </NFormItem>

            <NButton
              type="primary"
              size="large"
              block
              :loading="loading"
              class="submit-btn"
              @click="onSubmit"
            >
              {{ loading ? '登录中...' : '立即登录' }}
            </NButton>
          </NForm>

          <div class="card-footer">
            <span>还没有账号？</span>
            <RouterLink to="/register" class="link">立即注册</RouterLink>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  background:
    radial-gradient(ellipse at 30% 50%, rgba(0, 153, 255, 0.08) 0%, transparent 60%),
    radial-gradient(ellipse at 70% 50%, rgba(139, 92, 246, 0.06) 0%, transparent 60%),
    var(--landing-bg);
}

/* ===== Floating Blobs (same as HeroSection) ===== */
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
  top: 10%;
  left: 3%;
  animation: float 8s ease-in-out infinite;
}

.blob-2 {
  width: 120px;
  height: 120px;
  background: rgba(139, 92, 246, 0.12);
  top: 15%;
  right: 5%;
  animation: float 10s ease-in-out infinite;
}

.blob-3 {
  width: 80px;
  height: 80px;
  background: rgba(0, 210, 211, 0.15);
  bottom: 20%;
  left: 8%;
  animation: float 6s ease-in-out infinite;
}

.blob-4 {
  width: 150px;
  height: 150px;
  background: rgba(139, 92, 246, 0.1);
  bottom: 10%;
  right: 3%;
  animation: float 12s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(5deg); }
}

/* ===== Layout Container ===== */
.auth-container {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  gap: 80px;
  max-width: 1100px;
  width: 100%;
  padding: 40px 24px;
  animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
  from { opacity: 0; transform: translateY(24px); }
  to { opacity: 1; transform: translateY(0); }
}

/* ===== Left Branding ===== */
.auth-branding {
  flex: 1;
  max-width: 480px;
}

.brand-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 32px;
}

.brand-logo {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  background: var(--color-primary-gradient);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 800;
  letter-spacing: -0.5px;
}

.brand-text {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  color: var(--color-text-primary);
  letter-spacing: var(--letter-spacing-tight);
}

.branding-title {
  font-size: clamp(32px, 4vw, 44px);
  font-weight: 800;
  line-height: 1.2;
  margin: 0 0 20px;
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

.branding-desc {
  font-size: 16px;
  color: var(--color-text-secondary);
  line-height: 1.7;
  margin: 0 0 36px;
}

/* ===== Stats Row ===== */
.stats-row {
  display: flex;
  gap: 16px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  background: var(--landing-glass-bg);
  backdrop-filter: blur(var(--landing-blur-glass));
  border: 1px solid var(--landing-glass-border);
  border-radius: var(--landing-radius-card, 16px);
  box-shadow: var(--landing-shadow-card);
  transition: transform 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-icon {
  font-size: 22px;
  line-height: 1;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 16px;
  font-weight: 800;
  color: var(--color-text-primary);
  line-height: 1.2;
}

.stat-label {
  font-size: 11px;
  color: var(--color-text-secondary);
}

/* ===== Right Form Card ===== */
.auth-form-wrapper {
  width: 100%;
  max-width: 420px;
  flex-shrink: 0;
}

.auth-card {
  background: var(--landing-glass-bg);
  backdrop-filter: blur(var(--landing-blur-glass));
  border: 1px solid var(--landing-glass-border);
  border-radius: var(--landing-radius-card, 20px);
  box-shadow: var(--landing-shadow-card);
  padding: 40px 36px;
}

.card-header {
  text-align: center;
  margin-bottom: 28px;
}

.card-title {
  font-size: 24px;
  font-weight: 800;
  color: var(--color-text-primary);
  margin: 0 0 8px;
}

.card-subtitle {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  margin: 0;
}

.submit-btn {
  margin-top: 4px;
  font-weight: 600;
  border-radius: 12px !important;
  height: 44px !important;
  background: var(--landing-navy) !important;
  border-color: var(--landing-navy) !important;
  box-shadow: 0 4px 14px rgba(0, 67, 112, 0.25);
  transition: box-shadow 0.3s ease, transform 0.2s ease;
}

.submit-btn:hover {
  box-shadow: 0 6px 20px rgba(0, 67, 112, 0.35);
  transform: translateY(-1px);
  opacity: 0.9;
}

.submit-btn:active {
  transform: translateY(0);
}

.card-footer {
  text-align: center;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid var(--landing-glass-border);
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
}

.link {
  color: var(--landing-cyan, var(--color-primary));
  font-weight: 600;
  margin-left: 4px;
  transition: color 0.2s ease;
  text-decoration: none;
}

.link:hover {
  text-decoration: underline;
}

/* ===== Responsive ===== */
@media (max-width: 900px) {
  .auth-container {
    flex-direction: column;
    gap: 40px;
    text-align: center;
  }

  .auth-branding {
    max-width: 100%;
  }

  .branding-title {
    font-size: clamp(28px, 5vw, 36px);
  }

  .stats-row {
    justify-content: center;
    flex-wrap: wrap;
  }

  .auth-form-wrapper {
    max-width: 440px;
  }
}

@media (max-width: 480px) {
  .auth-card {
    padding: 28px 20px;
  }

  .stats-row {
    gap: 10px;
  }

  .stat-card {
    padding: 10px 12px;
  }
}
</style>
