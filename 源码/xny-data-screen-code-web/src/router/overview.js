export const overviewRoutes = [
  { path: '/country', name: 'country', component: () => import('@/page/country/index') },
  { path: '/province', name: 'province', component: () => import('@/page/province/index') },
  {
    path: '/weijiazhuang', name: 'weijiazhuang', component: () => import('@/page/jzsgf/view/pvGlobalOverview'),
    props: { weijiazhuang: true },
  },
]
