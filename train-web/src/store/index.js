import { createStore } from 'vuex'
const Member = "Member";

export default createStore({

  state: {
    member:window.SessionStorage.get(Member) || {}
  },
  getters: {
  },
  mutations: {
    setMember (state,_member){
      state.member = _member
      window.SessionStorage.set(Member,_member)
    }
  },
  actions: {
  },
  modules: {
  }
})
