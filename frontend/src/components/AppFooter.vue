<script setup lang="ts">
</script>

<template>
  <footer class="app-footer">
    <div class="footer-content">
      <div class="footer-top">
        <div class="footer-brand">
          <div class="footer-logo">C4</div>
          <div class="footer-brand-info">
            <span class="footer-name">CET-4 Practice</span>
            <p class="footer-desc">
              专注英语四级备考，助你轻松通过考试
            </p>
          </div>
        </div>
        <div class="footer-links">
          <a href="#" class="footer-link">关于我们</a>
          <a href="#" class="footer-link">使用帮助</a>
          <a href="#" class="footer-link">反馈建议</a>
        </div>
      </div>
      <div class="footer-bottom">
        <p class="footer-copyright">
          &copy; 2026 CET-4 Practice
        </p>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.app-footer {
  margin-top: auto;
  border-top: 1px solid var(--color-border-light);
  background: var(--color-bg-card);
}

.footer-content {
  max-width: var(--content-max-width);
  margin: 0 auto;
  padding: var(--spacing-8) var(--spacing-6) var(--spacing-6);
}

.footer-top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: var(--spacing-8);
  padding-bottom: var(--spacing-6);
  border-bottom: 1px solid var(--color-border-light);
}

.footer-brand {
  display: flex;
  align-items: flex-start;
  gap: var(--spacing-3);
}

.footer-logo {
  width: 32px;
  height: 32px;
  border-radius: var(--radius-md);
  background: var(--color-primary-gradient);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 800;
  letter-spacing: -0.5px;
  flex-shrink: 0;
}

.footer-brand-info {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-1);
}

.footer-name {
  font-weight: var(--font-weight-semibold);
  font-size: var(--font-size-base);
  color: var(--color-text-primary);
}

.footer-desc {
  color: var(--color-text-muted);
  font-size: var(--font-size-sm);
  margin: 0;
  line-height: var(--line-height-relaxed);
}

.footer-links {
  display: flex;
  align-items: center;
  gap: var(--spacing-6);
  flex-shrink: 0;
}

.footer-link {
  color: var(--color-text-muted);
  font-size: var(--font-size-sm);
  transition: color var(--transition-fast);
}

.footer-link:hover {
  color: var(--color-primary);
}

.footer-bottom {
  padding-top: var(--spacing-4);
}

.footer-copyright {
  color: var(--color-text-disabled);
  font-size: var(--font-size-xs);
  margin: 0;
}

@media (max-width: 768px) {
  .footer-content {
    padding: var(--spacing-6) var(--spacing-4) var(--spacing-4);
  }

  .footer-top {
    flex-direction: column;
    gap: var(--spacing-4);
  }

  .footer-links {
    gap: var(--spacing-4);
  }
}
</style>
