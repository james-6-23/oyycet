import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

import PaperListPage from '../pages/PaperListPage.vue'
import PaperDetailPage from '../pages/PaperDetailPage.vue'
import PracticePage from '../pages/PracticePage.vue'
import LoginPage from '../pages/LoginPage.vue'
import RegisterPage from '../pages/RegisterPage.vue'
import MyRecordsPage from '../pages/MyRecordsPage.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: () => import('../pages/LandingPage.vue'),
      meta: { layout: 'landing' },
    },
    { path: '/papers', component: PaperListPage },
    { path: '/paper/:id', component: PaperDetailPage, props: true },
    { path: '/paper/:id/practice', component: PracticePage, props: true },
    { path: '/login', component: LoginPage, meta: { layout: 'landing' } },
    { path: '/register', component: RegisterPage, meta: { layout: 'landing' } },
    { path: '/me/records', component: MyRecordsPage },
    {
      path: '/admin',
      component: () => import('../components/AdminLayout.vue'),
      meta: { requiresAdmin: true },
      children: [
        {
          path: '',
          name: 'AdminDashboard',
          component: () => import('../pages/admin/AdminDashboard.vue'),
        },
        {
          path: 'papers',
          name: 'AdminPapers',
          component: () => import('../pages/admin/AdminPapers.vue'),
        },
        {
          path: 'users',
          name: 'AdminUsers',
          component: () => import('../pages/admin/AdminUsers.vue'),
        },
      ],
    },
  ],
  scrollBehavior(to) {
    if (to.hash) {
      return { el: to.hash, behavior: 'smooth' }
    }
    return { top: 0 }
  },
})

router.beforeEach((to) => {
  if (to.meta.requiresAdmin || to.matched.some((r) => r.meta.requiresAdmin)) {
    const auth = useAuthStore()
    if (!auth.isLoggedIn) {
      return { path: '/login' }
    }
    if (auth.user?.role !== 'ADMIN') {
      return { path: '/papers' }
    }
  }
})

export default router
