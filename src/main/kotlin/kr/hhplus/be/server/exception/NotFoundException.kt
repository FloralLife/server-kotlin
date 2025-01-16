package kr.hhplus.be.server.exception

class NotFoundException : RuntimeException {
    companion object {
        const val MESSAGE_TEMPLATE = "%s not found. id: %s"
    }

    constructor(message: String) : super(message)

    constructor(id: Long, clazz: Class<*>) : super(MESSAGE_TEMPLATE.format(clazz.simpleName, id))

    constructor(ids: List<Long>, clazz: Class<*>) : super(MESSAGE_TEMPLATE.format(clazz.simpleName, ids))
}
