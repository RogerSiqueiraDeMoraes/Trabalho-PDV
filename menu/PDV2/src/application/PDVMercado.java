package application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PDVMercado extends JFrame {
    private static final long serialVersionUID = 1L;
	private List<ItemVenda> itens = new ArrayList<>();
    private DefaultListModel<String> listaModel = new DefaultListModel<>();
    private JList<String> listaItens;
    private JLabel lblTotal;
    private DecimalFormat df = new DecimalFormat("#,##0.00");

    public PDVMercado() {
        setTitle("PDV de Mercado - Sistema de Vendas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Painel principal
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel de produtos (esquerda)
        JPanel panelProdutos = new JPanel(new BorderLayout());
        panelProdutos.setBorder(BorderFactory.createTitledBorder("Produtos"));
        
        String[] produtos = {
            "Arroz 5kg - R$ 22,90", "Feijão 1kg - R$ 8,50", "Óleo 900ml - R$ 7,90",
            "Açúcar 5kg - R$ 15,00", "Café 500g - R$ 12,50", "Leite 1L - R$ 4,20",
            "Pão Francês - R$ 0,50", "Manteiga 200g - R$ 6,80", "Ovos (12) - R$ 10,00",
            "Carne Bovina 1kg - R$ 38,90", "Frango 1kg - R$ 15,50", "Peixe 1kg - R$ 25,00"
        };
        
        JList<String> listaProdutos = new JList<>(produtos);
        listaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollProdutos = new JScrollPane(listaProdutos);
        panelProdutos.add(scrollProdutos, BorderLayout.CENTER);
        
        JButton btnAdicionar = new JButton("Adicionar (F2)");
        btnAdicionar.addActionListener(e -> adicionarProduto(listaProdutos.getSelectedValue()));
        panelProdutos.add(btnAdicionar, BorderLayout.SOUTH);
        
        // Painel da venda (direita)
        JPanel panelVenda = new JPanel(new BorderLayout());
        panelVenda.setBorder(BorderFactory.createTitledBorder("Venda em Andamento"));
        
        listaItens = new JList<>(listaModel);
        JScrollPane scrollItens = new JScrollPane(listaItens);
        panelVenda.add(scrollItens, BorderLayout.CENTER);
        
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        panelTotal.add(lblTotal);
        panelVenda.add(panelTotal, BorderLayout.NORTH);
        
        JPanel panelBotoes = new JPanel(new GridLayout(1, 3, 5, 5));
        JButton btnRemover = new JButton("Remover (F3)");
        btnRemover.addActionListener(e -> removerItem());
        panelBotoes.add(btnRemover);
        
        JButton btnCancelar = new JButton("Cancelar Venda (F4)");
        btnCancelar.addActionListener(e -> cancelarVenda());
        panelBotoes.add(btnCancelar);
        
        JButton btnFinalizar = new JButton("Finalizar Venda (F1)");
        btnFinalizar.addActionListener(e -> finalizarVenda());
        btnFinalizar.setBackground(new Color(50, 150, 50));
        btnFinalizar.setForeground(Color.WHITE);
        panelBotoes.add(btnFinalizar);
        
        panelVenda.add(panelBotoes, BorderLayout.SOUTH);
        
        // Adicionando os painéis ao painel principal
        panel.add(panelProdutos, BorderLayout.WEST);
        panel.add(panelVenda, BorderLayout.CENTER);
        
        add(panel);
        
        // Configuração de atalhos
        setupAtalhos();
    }
    
    private void setupAtalhos() {
        // F1 - Finalizar venda
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke("F1"), "finalizarVenda");
        getRootPane().getActionMap().put("finalizarVenda", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizarVenda();
            }
        });
        
        // F2 - Adicionar produto
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke("F2"), "adicionarProduto");
        getRootPane().getActionMap().put("adicionarProduto", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Simula seleção do primeiro item se nenhum estiver selecionado
                adicionarProduto("Arroz 5kg - R$ 22,90");
            }
        });
        
        // F3 - Remover item
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke("F3"), "removerItem");
        getRootPane().getActionMap().put("removerItem", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerItem();
            }
        });
        
        // F4 - Cancelar venda
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke("F4"), "cancelarVenda");
        getRootPane().getActionMap().put("cancelarVenda", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarVenda();
            }
        });
    }
    
    private void adicionarProduto(String produtoSelecionado) {
        if (produtoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para adicionar!");
            return;
        }
        
        // Extrai nome e preço do produto
        String[] partes = produtoSelecionado.split(" - R\\$ ");
        String nome = partes[0];
        double preco = Double.parseDouble(partes[1].replace(",", "."));
        
        // Adiciona à lista de itens
        itens.add(new ItemVenda(nome, preco));
        
        // Atualiza a lista visual
        atualizarListaItens();
        
        // Atualiza o total
        calcularTotal();
    }
    
    private void removerItem() {
        int selectedIndex = listaItens.getSelectedIndex();
        if (selectedIndex != -1) {
            itens.remove(selectedIndex);
            atualizarListaItens();
            calcularTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item para remover!");
        }
    }
    
    private void cancelarVenda() {
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Cancelar toda a venda? Todos os itens serão removidos.",
            "Confirmar Cancelamento",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            itens.clear();
            listaModel.clear();
            calcularTotal();
        }
    }
    
    private void finalizarVenda() {
        if (itens.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione itens à venda antes de finalizar!");
            return;
        }
        
        double total = itens.stream().mapToDouble(ItemVenda::getPreco).sum();
        
        JOptionPane.showMessageDialog(
            this, 
            "Venda finalizada com sucesso!\nTotal: R$ " + df.format(total),
            "Venda Concluída",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Limpa a venda atual
        itens.clear();
        listaModel.clear();
        calcularTotal();
    }
    
    private void atualizarListaItens() {
        listaModel.clear();
        for (ItemVenda item : itens) {
            listaModel.addElement(item.getNome() + " - R$ " + df.format(item.getPreco()));
        }
    }
    
    private void calcularTotal() {
        double total = itens.stream().mapToDouble(ItemVenda::getPreco).sum();
        lblTotal.setText("Total: R$ " + df.format(total));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PDVMercado pdv = new PDVMercado();
            pdv.setVisible(true);
        });
    }
    
    // Classe interna para representar os itens da venda
    private class ItemVenda {
        private String nome;
        private double preco;
        
        public ItemVenda(String nome, double preco) {
            this.nome = nome;
            this.preco = preco;
        }
        
        public String getNome() {
            return nome;
        }
        
        public double getPreco() {
            return preco;
        }
    }
}