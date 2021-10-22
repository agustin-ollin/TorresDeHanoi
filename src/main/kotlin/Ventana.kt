import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.Image
import java.awt.event.ActionListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*

/**
 * Clase ventana, viene el código de la configuración de la ventana
 */
class Ventana(val titulo: String?) : JFrame(titulo) {
    // Creación de variables
    var panel = JPanel()
    val arreglo = Array<Int>(10, { i -> i + 1 })

    var combo_Cantidad = JComboBox(arreglo)
    var cuadro_Cantidad = JTextField()
    val boton_Iniciar = JButton("Iniciar cálculo")
    val boton_Reiniciar = JButton("Borrar Datos")

    var lista = JTextArea()
    var disparador = false

    val imagen_Salir = ImageIcon("src/main/kotlin/iconos/salir.png")
    val imagen_Informacion = ImageIcon("src/main/kotlin/iconos/informacion.png")
    val etiqueta_Salir = JLabel()
    val etiqueta_Información = JLabel()

    /**
     * Método para inicializar los componentes de la ventana
     */
    init {
        this.isUndecorated = true
        this.size = Dimension(500, 550)
        this.isVisible = true
        this.setLocationRelativeTo(null)
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.isResizable = false
        this.contentPane.add(panel)

        panel.layout = null
        panel.background = Color(0, 139, 139)

        // Ejecución de botones, etiquetas y eventos
        agregar_EtiquetasAccion()
        agregar_RegistroDatos()
        agregar_Lista()
        eventos()
    }

    /**
     * Se agregan las etiquetas con acciones, ambas contienen una imagen que representa su acción
     */
    fun agregar_EtiquetasAccion() {
        // Etiqueta para salir del programa
        panel.add(etiqueta_Salir)
        etiqueta_Salir.setBounds(425, 70, 50, 50)

        etiqueta_Salir.icon = ImageIcon(
            imagen_Salir.image.getScaledInstance(
                etiqueta_Salir.size.width,
                etiqueta_Salir.size.height,
                Image.SCALE_SMOOTH
            )
        )

        // Etiqueta para ver la revisar información sobre el juego
        panel.add(etiqueta_Información)
        etiqueta_Información.setBounds(425, 130, 50, 50)
        etiqueta_Información.icon = ImageIcon(
            imagen_Informacion.image.getScaledInstance(
                etiqueta_Información.size.width,
                etiqueta_Información.size.height,
                Image.SCALE_SMOOTH
            )
        )
    }

    /**
     * Sección de configuración de botones o componentes con los que interactúa el usuario
     * -ComboBox
     * -Etiquetas fijas
     * -Botones
     */
    fun agregar_RegistroDatos() {
        // Propiedades de sección principal
        val titulo = JLabel("Instrucciones para resolver las Torres de Hanói")
        panel.add(titulo)
        titulo.setBounds(30, 15, 420, 40)
        titulo.horizontalAlignment = SwingConstants.CENTER
        titulo.foreground = Color.BLACK
        titulo.font = Font(Font.DIALOG, Font.BOLD, 18)

        val etiqueta_CanDiscos = JLabel("Selecciona la cantidad de discos:")
        panel.add(etiqueta_CanDiscos)
        etiqueta_CanDiscos.setBounds(50, 70, 200, 30)

        panel.add(combo_Cantidad)
        combo_Cantidad.setBounds(250, 70, 100, 30)

        // Propiedades de sección que describe la cantidad de movimiento mínimos para solucionar el rompecabezas
        val etiqueta_Movimientos = JLabel("Cantidad de movimientos:")
        panel.add(etiqueta_Movimientos)
        etiqueta_Movimientos.setBounds(50, 120, 200, 30)

        panel.add(cuadro_Cantidad)
        cuadro_Cantidad.setBounds(250, 120, 100, 30)
        cuadro_Cantidad.isEditable = false

        // Propiedades de Botones
        panel.add(boton_Reiniciar)
        boton_Reiniciar.setBounds(75, 180, 150, 50)

        panel.add(boton_Iniciar)
        boton_Iniciar.setBounds(250, 180, 150, 50)
    }

    /**
     * Configuración de JTextArea donde aparecerán las indicaciones de solución
     */
    fun agregar_Lista() {
        lista.columns = 1
        lista.text = "Iniciar....."
        lista.isEnabled = true
        lista.isVisible = true
        lista.isEditable = false

        var scroll =
            JScrollPane(lista, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)

        scroll.setBounds(50, 250, 400, 250)
        panel.add(scroll)
    }

    /**
     * Método recursivo que genera las instrucciones necesarias, se le asignan 4 parámetros:
     * -Caracter A torre 1
     * -Caracter B torre 2
     * -Caracter C torre 3
     * -Número de piezas n
     */
    fun solucion_Recursiva(a: Char, b: Char, c: Char, n: Int) {
        if (n == 1) {
            lista.append("\n- Mover el disco de la Torre $a a la torre $c")
        } else {
            solucion_Recursiva(a, c, b, n - 1)
            lista.append("\n- Mover el disco de la Torre $a a la torre $c")
            solucion_Recursiva(b, a, c, n - 1)
        }
    }

    /**
     * Método para la asignación de los diferentes eventos que hay en los componentes
     */
    fun eventos() {
        var res: Int = 0
        var disparador = false

        // Botón para iniciar el cálculo y análisis de las instrucciones para resolver el juego
        boton_Iniciar.addActionListener(ActionListener {
            if (!disparador) {
                JOptionPane.showMessageDialog(
                    null,
                    "Selecciona primero la cantidad de Discos",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
                )
            } else {
                lista.text = "Pasos a seguir:"
                var n: Int = combo_Cantidad.selectedIndex + 1
                res = ((Math.pow(2.0, n.toDouble()) - 1).toInt())
                solucion_Recursiva('A', 'B', 'C', n)
                disparador = false
            }
        })

        //Botón para reiniciar los valores del programa
        boton_Reiniciar.addActionListener(ActionListener {
            combo_Cantidad.selectedIndex = 0
            lista.text = "Iniciar....."
            disparador = false
            cuadro_Cantidad.text = ""
        })

        //Acción sobre la selección de la cantidad de discos disponibles
        combo_Cantidad.addActionListener(ActionListener {
            var cont: Int = combo_Cantidad.selectedIndex + 1
            res = ((Math.pow(2.0, cont.toDouble()) - 1).toInt())
            cuadro_Cantidad.text = res.toString()

            disparador = true
        })

        //Eventos de tipo MouseListener
        eventos_Mouse()
    }

    /**
     * Método para la asignación de eventos a las etiquetas que contienen imágenes
     */
    fun eventos_Mouse() {
        //Etiqueta para salir del programa
        etiqueta_Salir.addMouseListener(object : MouseListener {
            override fun mouseClicked(e: MouseEvent?) {}
            override fun mouseEntered(e: MouseEvent?) {}
            override fun mousePressed(e: MouseEvent?) {}

            override fun mouseReleased(e: MouseEvent?) {
                JOptionPane.showMessageDialog(null, "Vuelva pronto", "SALIR", JOptionPane.INFORMATION_MESSAGE)
                System.exit(0)
            }

            override fun mouseExited(e: MouseEvent?) {}
        })

        //Etiqueta para abrir un cuadro de información
        etiqueta_Información.addMouseListener(object : MouseListener {
            override fun mouseClicked(e: MouseEvent?) {}
            override fun mouseEntered(e: MouseEvent?) {}
            override fun mousePressed(e: MouseEvent?) {}

            override fun mouseReleased(e: MouseEvent?) {
                val caja_Link = JTextArea()
                caja_Link.isEditable = false

                val info_Juego = "-El objetivo de las torres de Hanói es pasar los discos de la torre A a la torre C:" +
                        "\n  1.- Solo se puede mover un disco a la vez" +
                        "\n  2.- Un disco de mayor tamaño no puede estar sobre uno más pequeño" +
                        "\n  3.- Solo se puede desplazar el disco que se encuentre arriba en cada poste" +
                        "\n\nEste programa te muestra los movimientos que debes realizar para completar el juego" +
                        "\nsuponiendo que los tres postes son A-B-C." +
                        "\nPara comprobar las instrucciones puede entrar a este enlace:" +
                        "\nhttp://www.uterra.com/juegos/torre_hanoi.php "

                caja_Link.text = info_Juego
                JOptionPane.showMessageDialog(null, caja_Link, "INFORMACIÓN", JOptionPane.INFORMATION_MESSAGE)
            }

            override fun mouseExited(e: MouseEvent?) {}
        })
    }
}