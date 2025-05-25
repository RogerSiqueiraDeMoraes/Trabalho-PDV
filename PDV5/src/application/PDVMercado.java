package application;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
// Removido: import java.io.File;
import java.io.IOException;
import java.net.URL; // Import para carregar recurso
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * PDV (Ponto de Venda)
 */
public class PDVMercado extends JFrame {
    private static final long serialVersionUID = 1L;

    // --- CORES E FONTES ---
    private static final Color COR_FUNDO = new Color(245, 243, 220);
    private static final Color COR_PRINCIPAL = new Color(54, 94, 61);
    private static final Color COR_TITULO_APP = new Color(136, 176, 142);
    private static final Color COR_BOTAO_FINALIZAR = new Color(117, 195, 45);
    private static final Color COR_TEXTO_PADRAO = COR_PRINCIPAL;
    private static final Color COR_TEXTO_BOTAO = Color.WHITE;
    private static final Color COR_SELECAO = COR_TITULO_APP;
    private static final Font FONTE_PADRAO = new Font("Arial", Font.PLAIN, 14);
    private static final Font FONTE_BOLD = new Font("Arial", Font.BOLD, 14);
    private static final Font FONTE_TITULO_JANELA = new Font("Arial", Font.BOLD, 28);
    private static final Font FONTE_LISTA = new Font("Monospaced", Font.PLAIN, 14);
    private static final Font FONTE_TOTAL = new Font("Arial", Font.BOLD, 18);

    private List<ItemVenda> itens = new ArrayList<>();
    private DefaultListModel<String> listaModel = new DefaultListModel<>();
    private JList<String> listaItens;
    private JLabel lblTotal;
    private DecimalFormat df = new DecimalFormat("#,##0.00");
    private JList<String> listaProdutos;

    // --- IMAGEM DE FUNDO ---
    private BufferedImage backgroundImage;
    // Nome do arquivo da imagem (deve estar no mesmo pacote 'application')
    private static final String NOME_IMAGEM_FUNDO = "fundocaixa.jpeg";
    // ----------------------

    /**
     * Painel personalizado que desenha uma imagem de fundo.
     */
    class BackgroundPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private Image image;

        public BackgroundPanel(Image image) {
            this.image = image;
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public PDVMercado() {
        // Carrega a imagem de fundo como Recurso
        try {
            // Usa getClass().getResource() para buscar no mesmo pacote
            URL imageUrl = getClass().getResource(NOME_IMAGEM_FUNDO);
            if (imageUrl != null) {
                backgroundImage = ImageIO.read(imageUrl);
            } else {
                // Se não encontrar, lança uma exceção para o catch
                throw new IOException("Imagem '" + NOME_IMAGEM_FUNDO + "' não encontrada no pacote 'application'.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar a imagem de fundo: " + e.getMessage());
            backgroundImage = null; // Continua sem imagem se der erro
            // Opcional: Mostrar um JOptionPane para o usuário
            // JOptionPane.showMessageDialog(this, "Não foi possível carregar a imagem de fundo.", "Erro de Imagem", JOptionPane.ERROR_MESSAGE);
        }

        setTitle("Mercado Ideal - PDV");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
        setContentPane(backgroundPanel);

        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setOpaque(false); // <<< ESSENCIAL

        JLabel lblTituloApp = new JLabel("Mercado Ideal", SwingConstants.CENTER);
        lblTituloApp.setFont(FONTE_TITULO_JANELA);
        lblTituloApp.setForeground(COR_TITULO_APP);
        lblTituloApp.setBorder(new EmptyBorder(0, 0, 10, 0));
        lblTituloApp.setOpaque(false);
        panelPrincipal.add(lblTituloApp, BorderLayout.NORTH);

        JPanel panelProdutos = criarPanelProdutos();
        JPanel panelVenda = criarPanelVenda();

        panelPrincipal.add(panelProdutos, BorderLayout.WEST);
        panelPrincipal.add(panelVenda, BorderLayout.CENTER);

        backgroundPanel.add(panelPrincipal, BorderLayout.CENTER);

        setupAtalhos();
    }

    // --- MÉTODOS DE CRIAÇÃO E ESTILIZAÇÃO (criarBordaTitulo, estilizarScrollPane, etc.)

    private TitledBorder criarBordaTitulo(String titulo) {
        Border bordaLinha = BorderFactory.createLineBorder(COR_PRINCIPAL, 1, true);
        TitledBorder bordaTitulo = BorderFactory.createTitledBorder(bordaLinha, titulo);
        bordaTitulo.setTitleFont(FONTE_BOLD);
        bordaTitulo.setTitleColor(COR_PRINCIPAL);
        return bordaTitulo;
    }

    private void estilizarScrollPane(JScrollPane scrollPane) {
        scrollPane.getViewport().setBackground(COR_FUNDO);
        scrollPane.setBorder(BorderFactory.createLineBorder(COR_PRINCIPAL, 1));
    }

     private void estilizarLista(JList<?> list) {
         list.setBackground(COR_FUNDO);
         list.setForeground(COR_TEXTO_PADRAO);
         list.setSelectionBackground(COR_SELECAO);
         list.setSelectionForeground(Color.WHITE);
         list.setFont(FONTE_LISTA);
     }

    private void estilizarBotao(JButton button) {
        button.setBackground(COR_PRINCIPAL);
        button.setForeground(COR_TEXTO_BOTAO);
        button.setFont(FONTE_BOLD);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }

    private JPanel criarPanelProdutos() {
        JPanel panelProdutos = new JPanel(new BorderLayout(5, 10));
        panelProdutos.setBorder(criarBordaTitulo("Produtos Disponíveis"));
        panelProdutos.setBackground(COR_FUNDO);
        panelProdutos.setPreferredSize(new Dimension(280, 0));

        String[] produtos = {
            "Arroz 5kg - R$ 25,00", "Feijão 1kg - R$ 8,50", "Óleo 900ml - R$ 6,20",
            "Açúcar 1kg - R$ 4,30", "Café 500g - R$ 10,90", "Leite 1L - R$ 4,90",
            "Macarrão 500g - R$ 3,70", "Sabonete - R$ 2,50", "Detergente 500ml - R$ 3,00",
            "Papel Higiênico - R$ 12,00", "Pão Francês - R$ 0,50", "Manteiga 200g - R$ 6,80",
            "Ovos (12) - R$ 10,00", "Carne Bovina 1kg - R$ 38,90", "Frango 1kg - R$ 15,50"
        };

        listaProdutos = new JList<>(produtos);
        listaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaProdutos.setSelectedIndex(0);
        estilizarLista(listaProdutos);

        JScrollPane scrollProdutos = new JScrollPane(listaProdutos);
        estilizarScrollPane(scrollProdutos);
        panelProdutos.add(scrollProdutos, BorderLayout.CENTER);

        JButton btnAdicionar = new JButton("Adicionar (F2)");
        estilizarBotao(btnAdicionar);
        btnAdicionar.addActionListener(e -> adicionarProduto(listaProdutos.getSelectedValue()));

        JPanel panelBotaoAdd = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotaoAdd.setBackground(COR_FUNDO);
        panelBotaoAdd.add(btnAdicionar);
        panelProdutos.add(panelBotaoAdd, BorderLayout.SOUTH);

        return panelProdutos;
    }

    private JPanel criarPanelVenda() {
        JPanel panelVenda = new JPanel(new BorderLayout(5, 10));
        panelVenda.setBorder(criarBordaTitulo("Venda Atual"));
        panelVenda.setBackground(COR_FUNDO);

        JPanel panelTotal = new JPanel(new BorderLayout());
        panelTotal.setBackground(COR_PRINCIPAL);
        panelTotal.setBorder(new EmptyBorder(8, 15, 8, 15));

        JLabel lblListagemHeader = new JLabel("ITENS");
        lblListagemHeader.setFont(FONTE_BOLD);
        lblListagemHeader.setForeground(COR_TEXTO_BOTAO);
        panelTotal.add(lblListagemHeader, BorderLayout.WEST);

        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(FONTE_TOTAL);
        lblTotal.setForeground(COR_TEXTO_BOTAO);
        panelTotal.add(lblTotal, BorderLayout.EAST);
        panelVenda.add(panelTotal, BorderLayout.NORTH);

        listaItens = new JList<>(listaModel);
        estilizarLista(listaItens);
        JScrollPane scrollItens = new JScrollPane(listaItens);
        estilizarScrollPane(scrollItens);
        panelVenda.add(scrollItens, BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBotoes.setBackground(COR_FUNDO);

        JButton btnRemover = new JButton("Remover (F3)");
        estilizarBotao(btnRemover);
        btnRemover.addActionListener(e -> removerItem());
        panelBotoes.add(btnRemover);

        JButton btnCancelar = new JButton("Cancelar Venda (F4)");
        estilizarBotao(btnCancelar);
        btnCancelar.setBackground(new Color(192, 57, 43));
        btnCancelar.addActionListener(e -> cancelarVenda());
        panelBotoes.add(btnCancelar);

        JButton btnFinalizar = new JButton("Finalizar Venda (F1)");
        estilizarBotao(btnFinalizar);
        btnFinalizar.setBackground(COR_BOTAO_FINALIZAR);
        btnFinalizar.setForeground(Color.BLACK);
        btnFinalizar.addActionListener(e -> finalizarVenda());
        panelBotoes.add(btnFinalizar);

        panelVenda.add(panelBotoes, BorderLayout.SOUTH);

        return panelVenda;
    }

    // --- MÉTODOS DE FUNCIONALIDADE (setupAtalhos, adicionarProduto, etc.) ---

    private void setupAtalhos() {
        JRootPane rootPane = getRootPane();
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("F1"), "finalizarVenda");
        actionMap.put("finalizarVenda", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { finalizarVenda(); }
        });

        inputMap.put(KeyStroke.getKeyStroke("F2"), "adicionarProduto");
        actionMap.put("adicionarProduto", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarProduto(listaProdutos.getSelectedValue());
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("F3"), "removerItem");
        actionMap.put("removerItem", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { removerItem(); }
        });

        inputMap.put(KeyStroke.getKeyStroke("F4"), "cancelarVenda");
        actionMap.put("cancelarVenda", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { cancelarVenda(); }
        });
    }

    private void adicionarProduto(String produtoSelecionado) {
        if (produtoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para adicionar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String[] partes = produtoSelecionado.split(" - R\\$ ");
            String nome = partes[0];
            double preco = Double.parseDouble(partes[1].replace(",", "."));

            for (ItemVenda item : itens) {
                if (item.getNome().equals(nome)) {
                    item.setQuantidade(item.getQuantidade() + 1);
                    atualizarListaItens();
                    calcularTotal();
                    return;
                }
            }

            itens.add(new ItemVenda(nome, preco));
            atualizarListaItens();
            calcularTotal();
        } catch (Exception e) {
             JOptionPane.showMessageDialog(this, "Erro ao processar o produto selecionado.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerItem() {
        int selectedIndex = listaItens.getSelectedIndex();
        if (selectedIndex != -1) {
            ItemVenda item = itens.get(selectedIndex);
            if (item.getQuantidade() > 1) {
                item.setQuantidade(item.getQuantidade() - 1);
            } else {
                itens.remove(selectedIndex);
            }
            atualizarListaItens();
            calcularTotal();
            if (!itens.isEmpty()) {
               listaItens.setSelectedIndex(Math.min(selectedIndex, itens.size() - 1));
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cancelarVenda() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Cancelar toda a venda? Todos os itens serão removidos.",
            "Confirmar Cancelamento",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            itens.clear();
            listaModel.clear();
            calcularTotal();
        }
    }

    private void finalizarVenda() {
        if (itens.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione itens à venda antes de finalizar!", "Venda Vazia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = calcularValorTotal();
        String valorPagoStr = JOptionPane.showInputDialog(
            this,
            "Venda finalizada!\nTotal: R$ " + df.format(total) + "\n\nDigite o valor pago:",
            "Finalizar Venda",
            JOptionPane.INFORMATION_MESSAGE);

        if (valorPagoStr != null) {
            try {
                double valorPago = Double.parseDouble(valorPagoStr.replace(",", "."));
                double troco = valorPago - total;
                JOptionPane.showMessageDialog(
                    this,
                    "Venda Concluída!\nTotal: R$ " + df.format(total) + "\nPago: R$ " + df.format(valorPago) + "\nTroco: R$ " + df.format(troco),
                    "Venda Concluída",
                    JOptionPane.INFORMATION_MESSAGE);
             } catch (NumberFormatException e) {
                 JOptionPane.showMessageDialog(this, "Valor pago inválido. Venda será reiniciada.", "Erro", JOptionPane.ERROR_MESSAGE);
             }
        }

        itens.clear();
        listaModel.clear();
        calcularTotal();
    }

    private void atualizarListaItens() {
        listaModel.clear();
        for (int i = 0; i < itens.size(); i++) {
            ItemVenda item = itens.get(i);
            String linha = String.format("%-3d %-30s %3dx R$ %8s = R$ %8s",
                i + 1,
                item.getNome().length() > 28 ? item.getNome().substring(0, 28) + "..." : item.getNome(),
                item.getQuantidade(),
                df.format(item.getPreco()),
                df.format(item.getPreco() * item.getQuantidade()));
            listaModel.addElement(linha);
        }
    }

    private double calcularValorTotal() {
        return itens.stream()
            .mapToDouble(item -> item.getPreco() * item.getQuantidade())
            .sum();
    }

    private void calcularTotal() {
        double total = calcularValorTotal();
        lblTotal.setText("Total: R$ " + df.format(total));
    }

    // --- FIM MÉTODOS DE FUNCIONALIDADE ---

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> {
            PDVMercado pdv = new PDVMercado();
            pdv.setVisible(true);
        });
    }

    private class ItemVenda {
        private String nome;
        private double preco;
        private int quantidade;

        public ItemVenda(String nome, double preco) {
            this.nome = nome;
            this.preco = preco;
            this.quantidade = 1;
        }

        public String getNome() { return nome; }
        public double getPreco() { return preco; }
        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    }
}