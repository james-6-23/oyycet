<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { RouterLink } from 'vue-router'
import { NIcon } from 'naive-ui'
import { SunnyOutline, MoonOutline, MenuOutline, CloseOutline } from '@vicons/ionicons5'
import { useTheme } from '@/composables/useTheme'

const { isDark, toggleTheme } = useTheme()

const isScrolled = ref(false)
const showMobileMenu = ref(false)

const navLinks = [
  { label: '功能特性', href: '#features' },
  { label: '产品展示', href: '#product' },
  { label: '学员评价', href: '#testimonials' },
]

function handleScroll() {
  isScrolled.value = window.scrollY > 50
}

function scrollToAnchor(href: string) {
  showMobileMenu.value = false
  const id = href.replace('#', '')
  const el = document.getElementById(id)
  if (el) {
    el.scrollIntoView({ behavior: 'smooth' })
  }
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll, { passive: true })
  handleScroll()
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<template>
  <header class="landing-header" :class="{ scrolled: isScrolled }">
    <div class="header-inner">
      <!-- Brand -->
      <div class="brand">
        <div class="brand-logo">C4</div>
        <span class="brand-text">CET-4 Practice</span>
      </div>

      <!-- Desktop Nav -->
      <nav class="nav-desktop">
        <a
          v-for="link in navLinks"
          :key="link.href"
          class="nav-link"
          :href="link.href"
          @click.prevent="scrollToAnchor(link.href)"
        >
          {{ link.label }}
        </a>
      </nav>

      <!-- Actions -->
      <div class="header-actions">
        <button
          class="theme-toggle"
          :title="isDark ? '切换到浅色模式' : '切换到深色模式'"
          @click="toggleTheme"
        >
          <NIcon :size="18" :component="isDark ? SunnyOutline : MoonOutline" />
        </button>

        <RouterLink to="/papers" class="cta-btn">开始练习</RouterLink>

        <button
          class="mobile-menu-btn"
          @click="showMobileMenu = !showMobileMenu"
        >
          <NIcon :size="22" :component="showMobileMenu ? CloseOutline : MenuOutline" />
        </button>
      </div>
    </div>

    <!-- Mobile Menu Overlay -->
    <Transition name="mobile-menu">
      <div v-if="showMobileMenu" class="mobile-overlay">
        <nav class="mobile-nav">
          <a
            v-for="link in navLinks"
            :key="link.href"
            class="mobile-nav-link"
            :href="link.href"
            @click.prevent="scrollToAnchor(link.href)"
          >
            {{ link.label }}
          </a>
          <RouterLink to="/papers" class="mobile-cta" @click="showMobileMenu = false">
            开始练习
          </RouterLink>
        </nav>
      </div>
    </Transition>
  </header>
</template>

<style scoped>
/* ===== Header Shell ===== */
.landing-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 64px;
  z-index: 1000;
  backdrop-filter: blur(var(--landing-blur-glass));
  -webkit-backdrop-filter: blur(var(--landing-blur-glass));
  background: transparent;
  transition: background 0.3s ease, box-shadow 0.3s ease;
}

.landing-header.scrolled {
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 1px 0 rgba(0, 0, 0, 0.06);
}

/* ===== Inner Container ===== */
.header-inner {
  max-width: var(--landing-content-max);
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 var(--spacing-6);
  gap: var(--spacing-8);
}

/* ===== Brand ===== */
.brand {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  text-decoration: none;
  flex-shrink: 0;
}

.brand-logo {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: var(--color-primary-gradient);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 800;
  letter-spacing: -0.5px;
}

.brand-text {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  color: var(--color-text-primary);
  letter-spacing: var(--letter-spacing-tight);
}

/* ===== Desktop Nav ===== */
.nav-desktop {
  display: flex;
  align-items: center;
  gap: var(--spacing-1);
}

.nav-link {
  color: var(--color-text-secondary);
  text-decoration: none;
  font-weight: var(--font-weight-medium);
  font-size: var(--font-size-sm);
  padding: 6px 14px;
  border-radius: var(--radius-md);
  transition: color var(--transition-fast), background var(--transition-fast);
}

.nav-link:hover {
  color: var(--color-text-primary);
  background: var(--color-bg-subtle);
}

/* ===== Header Actions ===== */
.header-actions {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  flex-shrink: 0;
}

.theme-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  border: none;
  background: none;
  border-radius: var(--radius-md);
  color: var(--color-text-secondary);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.theme-toggle:hover {
  background: var(--color-bg-subtle);
  color: var(--color-text-primary);
}

/* ===== CTA Button ===== */
.cta-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 34px;
  padding: 0 18px;
  background: var(--color-primary-gradient);
  color: #fff;
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
  border-radius: var(--radius-full);
  text-decoration: none;
  transition: opacity var(--transition-fast), transform var(--transition-fast), box-shadow var(--transition-fast);
  box-shadow: var(--shadow-button);
}

.cta-btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(79, 110, 247, 0.3);
}

/* ===== Mobile Menu Button ===== */
.mobile-menu-btn {
  display: none;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  border: none;
  background: none;
  border-radius: var(--radius-md);
  color: var(--color-text-secondary);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.mobile-menu-btn:hover {
  background: var(--color-bg-subtle);
  color: var(--color-text-primary);
}

/* ===== Mobile Overlay ===== */
.mobile-overlay {
  position: absolute;
  top: 64px;
  left: 0;
  width: 100%;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(var(--landing-blur-glass));
  -webkit-backdrop-filter: blur(var(--landing-blur-glass));
  border-top: 1px solid var(--color-border-light);
  box-shadow: var(--shadow-dropdown);
}

.mobile-nav {
  display: flex;
  flex-direction: column;
  padding: var(--spacing-4) var(--spacing-6);
  gap: var(--spacing-1);
}

.mobile-nav-link {
  display: block;
  padding: var(--spacing-3) var(--spacing-4);
  color: var(--color-text-secondary);
  font-weight: var(--font-weight-medium);
  font-size: var(--font-size-base);
  text-decoration: none;
  border-radius: var(--radius-md);
  transition: color var(--transition-fast), background var(--transition-fast);
}

.mobile-nav-link:hover {
  color: var(--color-text-primary);
  background: var(--color-bg-subtle);
}

.mobile-cta {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: var(--spacing-2);
  padding: var(--spacing-3) var(--spacing-4);
  background: var(--color-primary-gradient);
  color: #fff;
  font-weight: var(--font-weight-semibold);
  font-size: var(--font-size-base);
  border-radius: var(--radius-md);
  text-decoration: none;
  transition: opacity var(--transition-fast);
}

.mobile-cta:hover {
  opacity: 0.9;
}

/* ===== Mobile Menu Transition ===== */
.mobile-menu-enter-active,
.mobile-menu-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}

.mobile-menu-enter-from,
.mobile-menu-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}

/* ===== Responsive ===== */
@media (max-width: 768px) {
  .header-inner {
    padding: 0 var(--spacing-4);
    gap: var(--spacing-4);
  }

  .brand-text {
    display: none;
  }

  .nav-desktop {
    display: none;
  }

  .cta-btn {
    display: none;
  }

  .mobile-menu-btn {
    display: flex;
  }
}

/* ===== Dark Mode ===== */
</style>

<style>
/* Unscoped dark mode overrides for landing header */
[data-theme="dark"] .landing-header.scrolled {
  background: rgba(15, 17, 23, 0.85);
  box-shadow: 0 1px 0 rgba(255, 255, 255, 0.04);
}

[data-theme="dark"] .landing-header .mobile-overlay {
  background: rgba(15, 17, 23, 0.95);
  border-top-color: var(--color-border);
}
</style>
