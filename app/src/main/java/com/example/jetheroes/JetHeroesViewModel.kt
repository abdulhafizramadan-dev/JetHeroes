package com.example.jetheroes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jetheroes.data.HeroRepository
import com.example.jetheroes.model.Hero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class JetHeroesViewModel(
    private val heroRepository: HeroRepository
) : ViewModel() {

    private val _groupedHeroes = MutableStateFlow(
        heroRepository.getHeroes()
            .sortedBy { it.name }
            .groupBy { it.name[0] }
    )

    val groupedHeroes: MutableStateFlow<Map<Char, List<Hero>>> get() =
        _groupedHeroes

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> get() = _query

    fun search(query: String) {
        _query.value = query
        _groupedHeroes.value = heroRepository.searchHeroes(query)
            .sortedBy { it.name }
            .groupBy { it.name[0] }
    }

}



@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val heroRepository: HeroRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(JetHeroesViewModel::class.java) ->
                JetHeroesViewModel(heroRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class : ${modelClass.name}")
        }
    }
}
