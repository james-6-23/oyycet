<script setup lang="ts">
import { ref, computed } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import { NButton, NIcon, NDropdown, NAvatar } from 'naive-ui'
import { SunnyOutline, MoonOutline, MenuOutline, PersonOutline, LogOutOutline, SettingsOutline } from '@vicons/ionicons5'
import { useTheme } from '@/composables/useTheme'
import { useAuthStore } from '@/stores/auth'
import MobileNav from './MobileNav.vue'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()
const { isDark, toggleTheme } = useTheme()

const showMobileNav = ref(false)

const isAdmin = computed(() => auth.user?.role === 'ADMIN')

const navItems = [
  { path: '/papers', label: '试卷' },
  { path: '/me/records', label: '记录' },
]

const userMenuOptions = [
  {
    label: '我的记录',
    key: 'records',
    icon: () => h(NIcon, null, { default: () => h(PersonOutline) })
  },
  {
    type: 'divider',
    key: 'd1'
  },
  {
    label: '退出登录',
    key: 'logout',
    icon: () => h(NIcon, null, { default: () => h(LogOutOutline) })
  }
]

function handleUserMenuSelect(key: string) {
  if (key === 'records') {
    router.push('/me/records')
  } else if (key === 'logout') {
    auth.logout()
    router.push('/')
  }
}

const isActive = (path: string) => {
  if (path === '/papers') return route.path === '/papers'
  return route.path.startsWith(path)
}

import { h } from 'vue'
</script>

<template>
  <header class="app-header">
    <div class="header-content">
      <RouterLink class="brand" to="/">
        <div class="brand-logo">C4</div>
        <span class="brand-text">CET-4 Practice</span>
      </RouterLink>

      <nav class="nav-desktop">
        <RouterLink
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          class="nav-link"
          :class="{ active: isActive(item.path) }"
        >
          {{ item.label }}
        </RouterLink>
      </nav>

      <div class="header-actions">
        <RouterLink v-if="isAdmin" to="/admin" class="admin-link">
          <NButton size="small" quaternary>
            <template #icon>
              <NIcon :size="16" :component="SettingsOutline" />
            </template>
            管理后台
          </NButton>
        </RouterLink>

        <button class="theme-toggle" @click="toggleTheme" :title="isDark ? '切换到浅色模式' : '切换到深色模式'">
          <NIcon :size="18" :component="isDark ? SunnyOutline : MoonOutline" />
        </button>

        <template v-if="auth.isLoggedIn">
          <NDropdown
            trigger="click"
            :options="userMenuOptions"
            @select="handleUserMenuSelect"
            placement="bottom-end"
          >
            <button class="user-btn">
              <NAvatar round :size="28" :style="{ background: 'var(--color-primary)', fontSize: '12px' }">
                {{ auth.user?.username?.charAt(0)?.toUpperCase() }}
              </NAvatar>
              <span class="user-name">{{ auth.user?.username }}</span>
            </button>
          </NDropdown>
        </template>
        <template v-else>
          <RouterLink to="/login">
            <NButton size="small" type="primary" :style="{ fontWeight: 500 }">登录</NButton>
          </RouterLink>
        </template>

        <button class="mobile-menu-btn" @click="showMobileNav = true">
          <NIcon :size="20" :component="MenuOutline" />
        </button>
      </div>
    </div>
  </header>

  <MobileNav v-model:show="showMobileNav" :nav-items="navItems" />
</template>

<style scoped>
.app-header {
  box-shadow: var(--shadow-header);
  position: sticky;
  top: 0;
  z-index: 100;
  height: var(--header-height);
  backdrop-filter: blur(12px);
  background: rgba(255, 255, 255, 0.85);
  border-bottom: 1px solid var(--color-border-light);
}

.header-content {
  max-width: var(--content-max-width);
  margin: 0 auto;
  padding: 0 var(--spacing-6);
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  gap: var(--spacing-8);
}

.brand {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  text-decoration: none;
  color: var(--color-text-primary);
  font-weight: var(--font-weight-bold);
  font-size: var(--font-size-lg);
  flex-shrink: 0;
}

.brand-logo {
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
}

.brand-text {
  font-size: var(--font-size-lg);
  letter-spacing: var(--letter-spacing-tight);
}

.nav-desktop {
  display: flex;
  gap: var(--spacing-1);
}

.nav-link {
  color: var(--color-text-secondary);
  text-decoration: none;
  font-weight: var(--font-weight-medium);
  font-size: var(--font-size-sm);
  padding: 6px 14px;
  border-radius: var(--radius-md);
  position: relative;
  transition: color var(--transition-fast), background var(--transition-fast);
}

.nav-link:hover {
  color: var(--color-text-primary);
  background: var(--color-bg-subtle);
}

.nav-link.active {
  color: var(--color-primary);
  background: var(--color-primary-lighter);
  font-weight: var(--font-weight-semibold);
}

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
  border-radius: var(--radius-md);
  color: var(--color-text-secondary);
  transition: all var(--transition-fast);
}

.theme-toggle:hover {
  background: var(--color-bg-subtle);
  color: var(--color-text-primary);
}

.user-btn {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  padding: 3px 10px 3px 3px;
  border-radius: var(--radius-full);
  transition: background var(--transition-fast);
  cursor: pointer;
}

.user-btn:hover {
  background: var(--color-bg-subtle);
}

.user-name {
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
}

.admin-link {
  text-decoration: none;
}

.mobile-menu-btn {
  display: none;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  border-radius: var(--radius-md);
  color: var(--color-text-secondary);
  transition: all var(--transition-fast);
}

.mobile-menu-btn:hover {
  background: var(--color-bg-subtle);
  color: var(--color-text-primary);
}

@media (max-width: 768px) {
  .header-content {
    padding: 0 var(--spacing-4);
    gap: var(--spacing-4);
  }

  .brand-text {
    display: none;
  }

  .nav-desktop {
    display: none;
  }

  .mobile-menu-btn {
    display: flex;
  }

  .user-name {
    display: none;
  }

  .user-btn {
    padding: 3px;
  }
}
</style>
