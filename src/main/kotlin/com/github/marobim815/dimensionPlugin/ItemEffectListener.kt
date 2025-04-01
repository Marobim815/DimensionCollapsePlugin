// Village.kt
data class Village(
    val name: String,
    val ijang: UUID,
    val residents: MutableListOf<UUID>,
    val villageHacks: MutableListOf<Core>,
    var villageLevel: Int = 1
)

// VillageManager.kt
object VillageManager {
    private val villages = MutableMapOf<Village>()
    private val pendingVillages = MutableMapOf<Village>
    
    fun registerVillage(player: Player, name: String, ijangUUID: UUID, residentsUUID: MutableListOf<UUID>) {
        if (pendingVillages.any { it.name != name }) {
            if (pendingVillages.any { it.ijang != ijangUUID || it.residents.any { !it.contains(residentsUUID) }) {
                    val village = Village(name=name, ijang=ijangUUID, residents=residentsUUID)
                    pendingVillages.add(village)
                    player.sendMessage(text("마을 대기열에 등록되었습니다!")) // todo: 메시지 (O)
                    
                } else {
                    player.sendMessage(text("주민이 다른 마을에 포함됩니다!")) // X
                    return
                }
                
            } else {
                player.sendMessage(text("이미 같은 이름의 마을이 존재합니다!").namedTextColor(RED)) // X
                return
            }
        }
    }
    
    
}