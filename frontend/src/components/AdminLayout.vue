<script setup lang="ts">
import { h, ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { NLayoutSider, NLayoutContent, NMenu, NButton, NIcon } from 'naive-ui'
import {
  GridOutline,
  DocumentTextOutline,
  PeopleOutline,
  ArrowBackOutline,
} from '@vicons/ionicons5'
import type { MenuOption } from 'naive-ui'

const router = useRouter()
const route = useRoute()
const collapsed = ref(false)

const activeKey = computed(() => {
  if (route.path.includes('/admin/papers')) return 'papers'
  if (route.path.includes('/admin/users')) return 'users'
  return 'dashboard'
})

const menuOptions: MenuOption[] = [
  {
    label: '仪表盘',
    key: 'dashboard',
    icon: () => h(NIcon, null, { default: () => h(GridOutline) }),
  },
  {
    label: '试卷管理',
    key: 'papers',
    icon: () => h(NIcon, null, { default: () => h(DocumentTextOutline) }),
  },
  {
    label: '用户管理',
    key: 'users',
    icon: () => h(NIcon, null, { default: () => h(PeopleOutline) }),
  },
]

function handleMenuSelect(key: string) {
  if (key === 'dashboard') router.push('/admin')
  else router.push(`/admin/${key}`)
}

function goBack() {
  router.push('/papers')
}
</script>

<template>
  <div class="admin-layout">
    <div class="admin-topbar">
      <div class="topbar-left">
        <span class="topbar-title">管理后台</span>
      </div>
      <div class="topbar-right">
        <NButton text @click="goBack">
          <template #icon>
            <NIcon :component="ArrowBackOutline" />
          </template>
          返回前台
        </NButton>
      </div>
    </div>
    <div class="admin-body">
      <NLayoutSider
        bordered
        :collapsed="collapsed"
        collapse-mode="width"
        :collapsed-width="64"
        :width="220"
        show-trigger
        @collapse="collapsed = true"
        @expand="collapsed = false"
        :native-scrollbar="false"
        class="admin-sider"
      >
        <NMenu
          :collapsed="collapsed"
          :collapsed-width="64"
          :collapsed-icon-size="22"
          :options="menuOptions"
          :value="activeKey"
          @update:value="handleMenuSelect"
        />
      </NLayoutSider>
      <NLayoutContent class="admin-content" :native-scrollbar="false">
        <router-view />
      </NLayoutContent>
    </div>
  </div>
</template>

<style scoped>
.admin-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--color-bg-subtle, #f5f7fa);
}

.admin-topbar {
  height: 56px;
  background: white;
  border-bottom: 1px solid var(--color-border-light, #e8e8e8);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  flex-shrink: 0;
}

.topbar-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text-primary, #1a1a1a);
}

.admin-body {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.admin-sider {
  flex-shrink: 0;
}

.admin-content {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
}
</style>
