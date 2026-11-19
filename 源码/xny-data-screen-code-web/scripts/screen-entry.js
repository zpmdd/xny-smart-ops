// Keep all pre-existing non-overview routes on the currently deployed application.
;(function () {
  const entries = /* BUILD_ENTRIES */ []
  const overviewPath = () => ['', '/', '/country', '/province', '/weijiazhuang'].includes(location.hash.slice(1).split('?')[0])
  const useOverview = overviewPath()
  let switching = false
  function switchEntry() {
    if (!switching && overviewPath() !== useOverview) {
      switching = true
      location.reload()
    }
  }
  window.addEventListener('hashchange', switchEntry)
  if (window.navigation) window.navigation.addEventListener('currententrychange', switchEntry)
  else window.setInterval(switchEntry, 200) // Older hash routers also navigate with history.pushState.

  const entry = entries[Number(useOverview)]
  for (const href of entry.styles) {
    const link = document.createElement('link')
    link.rel = 'stylesheet'
    link.href = href
    document.head.appendChild(link)
  }
  for (const item of entry.scripts) {
    const script = document.createElement('script')
    if (item.src) {
      script.async = false
      script.src = item.src
    } else {
      script.textContent = item.code
    }
    document.body.appendChild(script)
  }
})()
