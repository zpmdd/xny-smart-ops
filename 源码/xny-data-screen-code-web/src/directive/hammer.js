import Hammer from 'hammerjs'
const hammer = {
  bindings: {
    hammer: {
      type: Object,
      default: () => ({}),
    },
  },
  inserted(el, binding) {
    const hammer = binding.value
    const mc = new Hammer(el)
    mc.on(binding.arg, hammer)
  },
}

export default hammer
