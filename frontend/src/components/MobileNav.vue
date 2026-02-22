<script setup lang="ts">
import { NDrawer, NDrawerContent, NButton, NIcon } from 'naive-ui'
import { useRouter } from 'vue-router'
import { CloseOutline } from '@vicons/ionicons5'
import { useAuthStore } from '@/stores/auth'

interface NavItem {
  path: string
  label: string
}

interface Props {
  show: boolean
  navItems: NavItem[]
}

const props = defineProps<Props>()
const emit = defineEmits<{
  'update:show': [value: boolean]
}>()

const router = useRouter()
const auth = useAuthStore()

function close() {
  emit('update:show', false)
}

function navigate(path: string) {
  router.push(path)
  close()
}

function handleLogout() {
  auth.logout()
  close()
}
</script>

<template>
  <NDrawer :show="props.show" placement="right" :width="280" @update:show="emit('update:show', $event)">
    <NDrawerContent>
      <template #header>
        <div class="drawer-header">
          <span class="drawer-title">菜单</span>
          <NButton quaternary circle @click="close">
            <template #icon>
              <NIcon :component="CloseOutline" />
            </template>
          </NButton>
        </div>
      </template>

      <nav class="mobile-nav">
        <a
          v-for="item in navItems"
          :key="item.path"
          class="mobile-nav-link"
          @click="navigate(item.path)"
        >
          {{ item.label }}
        </a>
      </nav>

      <div class="mobile-nav-footer">
        <template v-if="auth.isLoggedIn">
          <div class="user-info">
            <span>👤</span>
            <span>{{ auth.user?.username }}</span>
          </div>
          <NButton block @click="handleLogout">退出登录</NButton>
        </template>
        <template v-else>
          <NButton block type="primary" @click="navigate('/login')">登录</NButton>
          <NButton block @click="navigate('/register')">注册</NButton>
        </template>
      </div>
    </NDrawerContent>
  </NDrawer>
</template>

<style scoped>
.drawer-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.drawer-title {
  font-weight: var(--font-weight-semibold);
  font-size: var(--font-size-lg);
}

.mobile-nav {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-2);
  padding: var(--spacing-4) 0;
}

.mobile-nav-link {
  display: block;
  padding: var(--spacing-3) var(--spacing-4);
  color: var(--color-text-primary);
  font-weight: var(--font-weight-medium);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: background var(--transition-fast);
}

.mobile-nav-link:hover {
  background: var(--color-bg-card-hover);
}

.mobile-nav-footer {
  margin-top: auto;
  padding-top: var(--spacing-6);
  border-top: 1px solid var(--color-border-light);
  display: flex;
  flex-direction: column;
  gap: var(--spacing-3);
}

.user-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  padding: var(--spacing-3);
  background: var(--color-bg-card-hover);
  border-radius: var(--radius-md);
  margin-bottom: var(--spacing-2);
}
</style>
