<script setup lang="ts">
import { ref, h, onMounted, reactive } from 'vue'
import {
  NDataTable, NButton, NSpace, NInput, NTag, NSwitch, NCard, NSelect,
  NPopconfirm, useMessage, type DataTableColumns,
} from 'naive-ui'
import type { AdminUser, PageResult } from '@/types/models'
import { getAdminUsers, updateUserRole, updateUserStatus } from '@/services/admin'
import { useAuthStore } from '@/stores/auth'

const message = useMessage()
const auth = useAuthStore()
const loading = ref(false)
const data = ref<AdminUser[]>([])
const pagination = reactive({
  page: 1,
  pageSize: 10,
  itemCount: 0,
  showSizePicker: true,
  pageSizes: [10, 20, 50],
})
const searchUsername = ref('')

async function loadData() {
  loading.value = true
  try {
    const res = await getAdminUsers({
      current: pagination.page,
      size: pagination.pageSize,
      username: searchUsername.value || undefined,
    })
    data.value = res.records
    pagination.itemCount = res.total
  } catch (e: any) {
    message.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function handlePageChange(page: number) {
  pagination.page = page
  loadData()
}

function handlePageSizeChange(pageSize: number) {
  pagination.pageSize = pageSize
  pagination.page = 1
  loadData()
}

function handleSearch() {
  pagination.page = 1
  loadData()
}

async function handleRoleChange(row: AdminUser, role: string) {
  try {
    await updateUserRole(row.id, role)
    message.success('角色更新成功')
    loadData()
  } catch (e: any) {
    message.error(e.message || '操作失败')
  }
}

async function handleStatusChange(row: AdminUser, value: boolean) {
  try {
    await updateUserStatus(row.id, value ? 1 : 0)
    message.success('状态更新成功')
    loadData()
  } catch (e: any) {
    message.error(e.message || '操作失败')
  }
}

const isSelf = (row: AdminUser) => row.id === auth.user?.id

const roleOptions = [
  { label: '学生', value: 'STUDENT' },
  { label: '管理员', value: 'ADMIN' },
]

const columns: DataTableColumns<AdminUser> = [
  { title: 'ID', key: 'id', width: 60 },
  { title: '用户名', key: 'username', width: 120 },
  { title: '邮箱', key: 'email', ellipsis: { tooltip: true } },
  { title: '昵称', key: 'nickname', width: 120 },
  {
    title: '角色',
    key: 'role',
    width: 140,
    render(row) {
      if (isSelf(row)) {
        return h(NTag, { type: 'info', size: 'small' }, { default: () => row.role === 'ADMIN' ? '管理员' : '学生' })
      }
      return h(NSelect, {
        value: row.role,
        options: roleOptions,
        size: 'small',
        style: 'width: 110px',
        onUpdateValue: (val: string) => handleRoleChange(row, val),
      })
    },
  },
  {
    title: '状态',
    key: 'status',
    width: 80,
    render(row) {
      return h(NSwitch, {
        value: row.status === 1,
        disabled: isSelf(row),
        onUpdateValue: (val: boolean) => handleStatusChange(row, val),
      })
    },
  },
  { title: '创建时间', key: 'createTime', width: 170 },
]

onMounted(loadData)
</script>

<template>
  <div class="users-page">
    <h2 class="page-title">用户管理</h2>
    <NCard>
      <div class="toolbar">
        <NSpace>
          <NInput
            v-model:value="searchUsername"
            placeholder="搜索用户名"
            clearable
            style="width: 200px"
            @keyup.enter="handleSearch"
          />
          <NButton type="primary" @click="handleSearch">搜索</NButton>
        </NSpace>
      </div>
      <NDataTable
        :columns="columns"
        :data="data"
        :loading="loading"
        :pagination="pagination"
        :row-key="(row: AdminUser) => row.id"
        remote
        @update:page="handlePageChange"
        @update:page-size="handlePageSizeChange"
      />
    </NCard>
  </div>
</template>

<style scoped>
.users-page {
  max-width: 1200px;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 20px;
  color: var(--color-text-primary, #1a1a1a);
}

.toolbar {
  margin-bottom: 16px;
}
</style>
