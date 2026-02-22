import { createRouter, createWebHistory } from 'vue-router'

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
  ],
  scrollBehavior(to) {
    if (to.hash) {
      return { el: to.hash, behavior: 'smooth' }
    }
    return { top: 0 }
  },
})

export default router
