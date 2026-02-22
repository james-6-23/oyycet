<script setup lang="ts">
const footerLinks = {
  product: {
    title: '产品',
    links: [
      { label: '功能特性', href: '#features' },
      { label: '试卷列表', href: '/papers' },
      { label: 'AI 解析', href: '#product' },
      { label: '评分系统', href: '#product' },
    ],
  },
  company: {
    title: '关于',
    links: [
      { label: '关于我们', href: '#' },
      { label: '联系方式', href: '#' },
      { label: '加入我们', href: '#' },
      { label: '博客', href: '#' },
    ],
  },
  legal: {
    title: '法律',
    links: [
      { label: '隐私政策', href: '#' },
      { label: '服务条款', href: '#' },
      { label: 'Cookie 政策', href: '#' },
    ],
  },
}
</script>

<template>
  <footer class="landing-footer">
    <div class="container">
      <div class="footer-grid">
        <!-- Brand Column -->
        <div class="footer-brand">
          <div class="brand-row">
            <div class="brand-logo">C4</div>
            <span class="brand-name">CET-4 Practice</span>
          </div>
          <p class="brand-desc">
            AI 驱动的英语四级智能练习平台，助你高效备考、轻松过级。
          </p>
        </div>

        <!-- Link Columns -->
        <div
          v-for="(section, key) in footerLinks"
          :key="key"
          class="footer-link-group"
        >
          <h4 class="link-group-title">{{ section.title }}</h4>
          <ul class="link-list">
            <li v-for="link in section.links" :key="link.label">
              <a :href="link.href" class="footer-link">{{ link.label }}</a>
            </li>
          </ul>
        </div>
      </div>

      <!-- Bottom Bar -->
      <div class="footer-bottom">
        <p class="copyright">&copy; 2026 CET-4 Practice. All rights reserved.</p>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.landing-footer {
  background: var(--landing-navy);
  color: rgba(255, 255, 255, 0.8);
}
/* PLACEHOLDER_FOOTER_STYLES */

.container {
  max-width: var(--landing-content-max);
  margin: 0 auto;
  padding: 60px 24px 24px;
}

.footer-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  gap: 48px;
}

.footer-brand {
  max-width: 300px;
}

.brand-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.brand-logo {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: linear-gradient(135deg, #6B8AFF 0%, #8DA5FF 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 800;
  letter-spacing: -0.5px;
  flex-shrink: 0;
}

.brand-name {
  font-size: 16px;
  font-weight: 700;
  color: #ffffff;
}

.brand-desc {
  font-size: 14px;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.6);
  margin: 0;
}

.link-group-title {
  font-size: 14px;
  font-weight: 600;
  color: #ffffff;
  margin: 0 0 16px;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.link-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.footer-link {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.6);
  text-decoration: none;
  transition: color 0.2s ease;
}

.footer-link:hover {
  color: #ffffff;
}

.footer-bottom {
  margin-top: 48px;
  padding-top: 24px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.copyright {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.4);
  margin: 0;
}

@media (max-width: 1024px) {
  .footer-grid {
    grid-template-columns: 1fr 1fr;
    gap: 32px;
  }

  .footer-brand {
    grid-column: 1 / -1;
    max-width: none;
  }
}

@media (max-width: 768px) {
  .container {
    padding: 40px 16px 16px;
  }

  .footer-grid {
    grid-template-columns: 1fr;
    gap: 24px;
  }
}
</style>
