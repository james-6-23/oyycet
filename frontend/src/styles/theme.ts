import type { GlobalThemeOverrides } from 'naive-ui'

// 现代主题配置 - 浅色模式
export const lightThemeOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#4F6EF7',
    primaryColorHover: '#6B8AFF',
    primaryColorPressed: '#3B55D9',
    primaryColorSuppl: '#4F6EF7',
    borderRadius: '8px',
    fontFamily: 'Inter, -apple-system, BlinkMacSystemFont, "PingFang SC", "Microsoft YaHei", sans-serif',
    fontSize: '14px',
    bodyColor: '#F5F7FA',
    cardColor: '#FFFFFF',
    textColorBase: '#1A1D23',
    textColor1: '#1A1D23',
    textColor2: '#5F6571',
    textColor3: '#9CA3AF',
    borderColor: '#E5E7EB',
    dividerColor: '#E5E7EB',
    hoverColor: '#F5F7FA',
    successColor: '#10B981',
    successColorHover: '#34D399',
    successColorPressed: '#059669',
    warningColor: '#F59E0B',
    warningColorHover: '#FBBF24',
    warningColorPressed: '#D97706',
    errorColor: '#EF4444',
    errorColorHover: '#F87171',
    errorColorPressed: '#DC2626',
    infoColor: '#4F6EF7',
    infoColorHover: '#6B8AFF',
    infoColorPressed: '#3B55D9',
  },
  Button: {
    borderRadiusMedium: '8px',
    borderRadiusLarge: '10px',
    borderRadiusSmall: '6px',
    heightMedium: '38px',
    heightLarge: '44px',
    heightSmall: '32px',
    paddingMedium: '0 20px',
    fontWeightStrong: '600',
  },
  Card: {
    borderRadius: '12px',
    paddingMedium: '20px',
    paddingLarge: '28px',
    borderColor: '#F0F1F3',
  },
  Input: {
    borderRadius: '8px',
    heightMedium: '38px',
    heightLarge: '44px',
  },
  Form: {
    labelFontWeight: '500',
    labelFontSizeTopMedium: '13px',
  },
  Tag: {
    borderRadius: '6px',
    heightMedium: '28px',
  },
  Message: {
    borderRadius: '8px',
  },
  Dialog: {
    borderRadius: '12px',
  },
  DataTable: {
    borderRadius: '8px',
    thColor: '#F9FAFB',
  },
  Tabs: {
    tabFontWeightActive: '600',
  },
}

// 现代主题配置 - 深色模式
export const darkThemeOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#6B8AFF',
    primaryColorHover: '#8DA5FF',
    primaryColorPressed: '#4F6EF7',
    primaryColorSuppl: '#6B8AFF',
    bodyColor: '#0F1117',
    cardColor: '#1A1D27',
    modalColor: '#1A1D27',
    popoverColor: '#1A1D27',
    tableColor: '#1A1D27',
    textColorBase: '#E8EAED',
    textColor1: '#E8EAED',
    textColor2: '#9AA0AB',
    textColor3: '#6B7280',
    borderColor: '#2A2D37',
    dividerColor: '#2A2D37',
    hoverColor: '#22252F',
    inputColor: '#151820',
    actionColor: '#151820',
  },
  Card: {
    borderColor: '#2A2D37',
    color: '#1A1D27',
  },
  DataTable: {
    thColor: '#151820',
    tdColor: '#1A1D27',
  },
}
