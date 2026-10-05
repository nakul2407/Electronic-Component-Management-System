import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ElectronicComponentSystem extends JFrame {

    final Color NAVY=new Color(25,45,70), BLUE=new Color(45,105,170);
    final Color BG=new Color(245,247,250), TEXT=new Color(35,45,55);
    final Color BORDER=new Color(215,220,228);

    CardLayout cl=new CardLayout();
    JPanel pages=new JPanel(cl);
    ArrayList<Record> records=new ArrayList<>();
    int next=1001;

    final String FILE=System.getProperty("user.home")+File.separator+
            "ElectronicComponentData.dat";

    String[] branches={"EXTC","ECS","CS","IT","Mechanical","Automobile"};

    JTextField student,roll,component,issueDate,returnDate;
    JComboBox<String> branch,pending,condition;
    DefaultTableModel transactionModel,receiveModel,inventoryModel;
    JLabel total,pendingCount,returned;

    public ElectronicComponentSystem(){
        load();
        setTitle("Electronic Component Issue and Inventory Management System");
        setSize(1150,700);
        setMinimumSize(new Dimension(1000,620));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        header();
        sidebar();

        pages.add(dashboard(),"D");
        pages.add(issuePage(),"I");
        pages.add(receivePage(),"R");
        pages.add(transactionPage(),"T");
        pages.add(inventoryPage(),"V");

        add(pages,BorderLayout.CENTER);
        show("D");
    }

    // ---------------- HEADER ----------------

    void header(){
        JPanel p=new JPanel(new BorderLayout());
        p.setBackground(NAVY);
        p.setBorder(new EmptyBorder(18,25,18,25));

        JLabel t=label("ELECTRONIC COMPONENT MANAGEMENT SYSTEM",22,true);
        JLabel a=label("Administrator",15,true);
        t.setForeground(Color.WHITE);
        a.setForeground(Color.WHITE);

        p.add(t,BorderLayout.WEST);
        p.add(a,BorderLayout.EAST);
        add(p,BorderLayout.NORTH);
    }

    // ---------------- SIDEBAR ----------------

    void sidebar(){
        JPanel p=new JPanel();
        p.setBackground(NAVY);
        p.setPreferredSize(new Dimension(205,0));
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(25,10,20,10));

        JLabel m=label("MAIN MENU",15,true);
        m.setForeground(Color.WHITE);
        m.setAlignmentX(.5f);
        p.add(m);
        p.add(Box.createVerticalStrut(25));

        nav(p,"Dashboard","D");
        nav(p,"Issue Component","I");
        nav(p,"Receive Component","R");
        nav(p,"Transactions","T");
        nav(p,"Inventory","V");

        add(p,BorderLayout.WEST);
    }

    void nav(JPanel p,String text,String page){
        MyButton b=new MyButton(text,180,46);
        b.setAlignmentX(.5f);
        b.addActionListener(e->show(page));
        p.add(b);
        p.add(Box.createVerticalStrut(12));
    }

    // ---------------- DASHBOARD ----------------

    JPanel dashboard(){
        JPanel main=page();
        main.add(label("Dashboard",27,true),BorderLayout.NORTH);

        JPanel center=new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.add(Box.createVerticalStrut(20));

        JPanel cards=new JPanel(new GridLayout(1,3,20,0));
        cards.setOpaque(false);
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE,145));

        total=number();
        pendingCount=number();
        returned=number();

        cards.add(card("Total Transactions",total));
        cards.add(card("Pending Returns",pendingCount));
        cards.add(card("Returned Components",returned));

        center.add(cards);
        center.add(Box.createVerticalStrut(35));

        JPanel welcome=new JPanel(new GridBagLayout());
        welcome.setBackground(Color.WHITE);
        welcome.setBorder(new CompoundBorder(
                new LineBorder(BORDER),
                new EmptyBorder(25,20,25,20)));

        JLabel w=label(
                "Welcome to Electronics Component Library",
                23,true);
        w.setForeground(NAVY);
        welcome.add(w);
        welcome.setMaximumSize(new Dimension(Integer.MAX_VALUE,150));

        center.add(welcome);
        center.add(Box.createVerticalStrut(25));
        center.add(Box.createVerticalGlue());

        main.add(center,BorderLayout.CENTER);
        updateDashboard();
        return main;
    }

    JLabel number(){
        JLabel l=new JLabel("0",SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI",Font.BOLD,36));
        l.setForeground(BLUE);
        return l;
    }

    JPanel card(String text,JLabel value){
        JPanel p=new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(
                new LineBorder(BORDER),
                new EmptyBorder(15,15,15,15)));

        JLabel t=label(text,15,true);
        t.setHorizontalAlignment(SwingConstants.CENTER);

        p.add(t,BorderLayout.NORTH);
        p.add(value,BorderLayout.CENTER);
        return p;
    }

    // ---------------- ISSUE PAGE ----------------

    JPanel issuePage(){
        JPanel main=page();
        main.add(label("Issue Component",27,true),BorderLayout.NORTH);

        JPanel f=new JPanel(new GridBagLayout());
        f.setBackground(Color.WHITE);
        f.setBorder(new CompoundBorder(
                new LineBorder(BORDER),
                new EmptyBorder(30,40,30,40)));

        GridBagConstraints g=new GridBagConstraints();
        g.insets=new Insets(10,10,10,10);
        g.fill=GridBagConstraints.HORIZONTAL;

        student=field();
        roll=field();
        component=field();
        issueDate=field();
        issueDate.setEditable(false);

        branch=new JComboBox<>(branches);
        combo(branch);

        addField(f,g,0,"Student Name",student);
        addField(f,g,1,"Roll No.",roll);
        addField(f,g,2,"Branch",branch);
        addField(f,g,3,"Component Name",component);

        g.gridy=4;
        g.gridx=0;
        g.weightx=0;
        f.add(label("Issue Date",14,true),g);

        JPanel dp=new JPanel(new BorderLayout(10,0));
        dp.setOpaque(false);

        MyButton db=new MyButton("Select Date",125,42);
        db.addActionListener(e->date(issueDate));

        dp.add(issueDate,BorderLayout.CENTER);
        dp.add(db,BorderLayout.EAST);

        g.gridx=1;
        g.weightx=1;
        f.add(dp,g);

        MyButton issue=new MyButton("Issue Component",200,45);
        issue.addActionListener(e->issue());

        g.gridy=5;
        f.add(issue,g);

        main.add(f,BorderLayout.CENTER);
        return main;
    }

    void addField(JPanel p,GridBagConstraints g,int y,
                  String text,JComponent c){
        g.gridy=y;
        g.gridx=0;
        g.weightx=0;
        p.add(label(text,14,true),g);

        g.gridx=1;
        g.weightx=1;
        p.add(c,g);
    }

    void issue(){
        if(student.getText().trim().isEmpty() ||
           roll.getText().trim().isEmpty() ||
           component.getText().trim().isEmpty() ||
           issueDate.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this,"Please fill all details.");
            return;
        }

        Record r=new Record();
        r.id=next++;
        r.student=student.getText().trim();
        r.roll=roll.getText().trim();
        r.branch=branch.getSelectedItem().toString();
        r.component=component.getText().trim();
        r.issue=issueDate.getText();
        r.status="Pending";

        records.add(r);
        save();

        student.setText("");
        roll.setText("");
        component.setText("");
        issueDate.setText("");

        update();

        JOptionPane.showMessageDialog(this,
                "Component issued successfully!\nTransaction ID: "+r.id);
    }

    // ---------------- RECEIVE PAGE ----------------

    JPanel receivePage(){
        JPanel main=page();
        main.add(label("Receive Component",27,true),BorderLayout.NORTH);

        JPanel top=new JPanel(new GridBagLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(new CompoundBorder(
                new LineBorder(BORDER),
                new EmptyBorder(25,30,25,30)));

        GridBagConstraints g=new GridBagConstraints();
        g.insets=new Insets(10,10,10,10);
        g.fill=GridBagConstraints.HORIZONTAL;

        pending=new JComboBox<>();
        combo(pending);

        g.gridx=0;
        g.gridy=0;
        top.add(label("Pending Transaction",14,true),g);

        g.gridx=1;
        g.weightx=1;
        top.add(pending,g);

        MyButton search=new MyButton("Search Transaction",170,42);
        search.addActionListener(e->refreshPending());

        g.gridx=2;
        g.weightx=0;
        top.add(search,g);

        returnDate=field();
        returnDate.setEditable(false);

        g.gridx=0;
        g.gridy=1;
        top.add(label("Return Date",14,true),g);

        JPanel dp=new JPanel(new BorderLayout(10,0));
        dp.setOpaque(false);

        MyButton db=new MyButton("Select Date",125,42);
        db.addActionListener(e->date(returnDate));

        dp.add(returnDate,BorderLayout.CENTER);
        dp.add(db,BorderLayout.EAST);

        g.gridx=1;
        g.weightx=1;
        top.add(dp,g);

        condition=new JComboBox<>(
                new String[]{"Good","Minor Damage","Damaged"});
        combo(condition);

        g.gridx=0;
        g.gridy=2;
        g.weightx=0;
        top.add(label("Component Condition",14,true),g);

        g.gridx=1;
        g.weightx=1;
        top.add(condition,g);

        MyButton receive=new MyButton(
                "Receive Selected Component",230,45);
        receive.addActionListener(e->receive());

        g.gridx=1;
        g.gridy=3;
        g.weightx=0;
        top.add(receive,g);

        main.add(top,BorderLayout.NORTH);

        receiveModel=model(new String[]{
                "Transaction ID","Student","Roll No.",
                "Branch","Component","Issue Date","Status"
        });

        JTable table=new JTable(receiveModel);
        tableStyle(table);

        main.add(new JScrollPane(table),BorderLayout.CENTER);
        return main;
    }

    void receive(){
        if(pending.getSelectedItem()==null){
            JOptionPane.showMessageDialog(this,
                    "No pending transaction selected.");
            return;
        }

        if(returnDate.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Please select return date.");
            return;
        }

        int id=Integer.parseInt(
                pending.getSelectedItem().toString().split(" - ")[0]);

        for(Record r:records){
            if(r.id==id && r.status.equals("Pending")){
                r.returnDate=returnDate.getText();
                r.condition=condition.getSelectedItem().toString();
                r.status="Returned";

                save();
                returnDate.setText("");
                update();

                JOptionPane.showMessageDialog(this,
                        "Component received successfully!");
                return;
            }
        }
    }

    // ---------------- TRANSACTIONS ----------------

    JPanel transactionPage(){
        JPanel main=page();
        main.add(label("Transactions",27,true),BorderLayout.NORTH);

        transactionModel=model(new String[]{
                "Transaction ID","Student Name","Roll No.",
                "Branch","Component","Issue Date",
                "Return Date","Condition","Status"
        });

        JTable table=new JTable(transactionModel);
        tableStyle(table);

        main.add(new JScrollPane(table),BorderLayout.CENTER);

        JPanel bottom=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);

        MyButton clear=new MyButton("Clear All Data",170,45);
        clear.addActionListener(e->clearAllData());

        bottom.add(clear);
        main.add(bottom,BorderLayout.SOUTH);

        return main;
    }

    // ---------------- INVENTORY ----------------

    JPanel inventoryPage(){
        JPanel main=page();
        main.add(label("Inventory",27,true),BorderLayout.NORTH);

        inventoryModel=model(new String[]{
                "Component Name","Total Issued","Pending","Returned"
        });

        JTable table=new JTable(inventoryModel);
        tableStyle(table);

        main.add(new JScrollPane(table),BorderLayout.CENTER);
        return main;
    }

    // ---------------- UPDATE ----------------

    void update(){
        updateDashboard();
        refreshPending();

        if(transactionModel!=null){
            transactionModel.setRowCount(0);

            for(Record r:records)
                transactionModel.addRow(new Object[]{
                        r.id,r.student,r.roll,r.branch,r.component,
                        r.issue,r.returnDate,r.condition,r.status
                });
        }

        if(inventoryModel!=null){
            inventoryModel.setRowCount(0);

            LinkedHashMap<String,int[]> map=new LinkedHashMap<>();

            for(Record r:records){
                int[] v=map.computeIfAbsent(
                        r.component,k->new int[3]);

                v[0]++;
                if(r.status.equals("Pending")) v[1]++;
                else v[2]++;
            }

            for(Map.Entry<String,int[]> e:map.entrySet())
                inventoryModel.addRow(new Object[]{
                        e.getKey(),
                        e.getValue()[0],
                        e.getValue()[1],
                        e.getValue()[2]
                });
        }
    }

    void refreshPending(){
        if(pending==null) return;

        pending.removeAllItems();

        if(receiveModel!=null)
            receiveModel.setRowCount(0);

        for(Record r:records){
            if(r.status.equals("Pending")){
                pending.addItem(
                        r.id+" - "+r.student+" - "+r.component);

                if(receiveModel!=null)
                    receiveModel.addRow(new Object[]{
                            r.id,r.student,r.roll,r.branch,
                            r.component,r.issue,r.status
                    });
            }
        }
    }

    void updateDashboard(){
        if(total==null) return;

        int p=0,r=0;

        for(Record x:records){
            if(x.status.equals("Pending")) p++;
            else r++;
        }

        total.setText(""+records.size());
        pendingCount.setText(""+p);
        returned.setText(""+r);
    }

    // ---------------- CLEAR DATA ----------------

    void clearAllData(){
        if(records.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "There is no data to delete.");
            return;
        }

        int c=JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete ALL transaction data?"
                +"\nThis action cannot be undone.",
                "Clear All Data",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if(c!=JOptionPane.YES_OPTION) return;

        records.clear();
        next=1001;
        save();
        update();

        JOptionPane.showMessageDialog(this,
                "All data has been cleared successfully."
                +"\nTransaction ID reset to 1001.");
    }

    // ---------------- TABLE ----------------

    DefaultTableModel model(String[] cols){
        return new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r,int c){
                return false;
            }
        };
    }

    void tableStyle(JTable t){
        t.setFont(new Font("Segoe UI",Font.PLAIN,13));
        t.setRowHeight(32);
        t.setAutoCreateRowSorter(true);

        t.getTableHeader().setPreferredSize(
                new Dimension(0,40));

        t.getTableHeader().setDefaultRenderer(
                (table,value,selected,focus,row,col)->{
                    JLabel l=new JLabel(
                            value==null?"":value.toString(),
                            SwingConstants.CENTER);

                    l.setFont(new Font(
                            "Segoe UI",Font.BOLD,13));
                    l.setForeground(Color.WHITE);
                    l.setBackground(NAVY);
                    l.setOpaque(true);

                    l.setBorder(new MatteBorder(
                            0,0,1,1,
                            new Color(70,90,115)));

                    return l;
                });
    }

    // ---------------- CALENDAR ----------------

    void date(JTextField target){
        JDialog d=new JDialog(this,"Select Date",true);
        d.setSize(390,340);
        d.setResizable(false);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout(8,8));

        Calendar cal=Calendar.getInstance();

        JLabel month=new JLabel(
                "",SwingConstants.CENTER);
        month.setFont(new Font(
                "Segoe UI",Font.BOLD,18));
        month.setForeground(TEXT);

        DayButton prev=new DayButton("<");
        DayButton nxt=new DayButton(">");

        JPanel top=new JPanel(new BorderLayout(10,0));
        top.setBackground(Color.WHITE);
        top.setBorder(new EmptyBorder(8,10,5,10));

        top.add(prev,BorderLayout.WEST);
        top.add(month,BorderLayout.CENTER);
        top.add(nxt,BorderLayout.EAST);

        d.add(top,BorderLayout.NORTH);

        JPanel body=new JPanel(new BorderLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(0,10,10,10));

        JPanel week=new JPanel(new GridLayout(1,7,3,3));
        week.setBackground(Color.WHITE);

        for(String s:new String[]{
                "Sun","Mon","Tue","Wed","Thu","Fri","Sat"}){
            JLabel l=new JLabel(s,SwingConstants.CENTER);
            l.setFont(new Font(
                    "Segoe UI",Font.BOLD,12));
            l.setForeground(NAVY);
            week.add(l);
        }

        body.add(week,BorderLayout.NORTH);

        JPanel dates=new JPanel(new GridLayout(6,7,3,3));
        dates.setBackground(Color.WHITE);
        body.add(dates,BorderLayout.CENTER);
        d.add(body,BorderLayout.CENTER);

        Runnable refresh=()->{
            dates.removeAll();

            month.setText(new SimpleDateFormat(
                    "MMMM yyyy").format(cal.getTime()));

            Calendar first=(Calendar)cal.clone();
            first.set(Calendar.DAY_OF_MONTH,1);

            int start=first.get(Calendar.DAY_OF_WEEK)-1;
            int max=cal.getActualMaximum(
                    Calendar.DAY_OF_MONTH);

            for(int i=0;i<start;i++)
                dates.add(new JLabel(""));

            for(int day=1;day<=max;day++){
                final int selectedDay=day;

                DayButton b=new DayButton(""+day);
                b.addActionListener(e->{
                    Calendar selected=(Calendar)cal.clone();

                    selected.set(
                            Calendar.DAY_OF_MONTH,
                            selectedDay);

                    target.setText(
                            new SimpleDateFormat(
                                    "dd-MM-yyyy")
                                    .format(selected.getTime()));

                    d.dispose();
                });

                dates.add(b);
            }

            while(dates.getComponentCount()<42)
                dates.add(new JLabel(""));

            dates.revalidate();
            dates.repaint();
        };

        prev.addActionListener(e->{
            cal.add(Calendar.MONTH,-1);
            refresh.run();
        });

        nxt.addActionListener(e->{
            cal.add(Calendar.MONTH,1);
            refresh.run();
        });

        refresh.run();
        d.setVisible(true);
    }

    // ---------------- CALENDAR BUTTON ----------------

    class DayButton extends JButton{
        DayButton(String s){
            super(s);
            setFont(new Font(
                    "Segoe UI",Font.BOLD,12));
            setForeground(TEXT);
            setPreferredSize(new Dimension(45,35));
            setMinimumSize(new Dimension(45,35));
            setMaximumSize(new Dimension(45,35));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
        }

        protected void paintComponent(Graphics g){
            Graphics2D x=(Graphics2D)g.create();

            x.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Color c=Color.WHITE;

            if(getModel().isPressed())
                c=new Color(220,230,240);
            else if(getModel().isRollover())
                c=new Color(235,242,250);

            x.setColor(c);
            x.fillRoundRect(
                    0,0,getWidth()-1,getHeight()-1,6,6);

            x.setColor(new Color(190,200,210));
            x.drawRoundRect(
                    0,0,getWidth()-1,getHeight()-1,6,6);

            String s=getText();
            x.setFont(getFont());
            x.setColor(TEXT);

            FontMetrics fm=x.getFontMetrics();

            int xx=(getWidth()-fm.stringWidth(s))/2;
            int yy=(getHeight()-fm.getHeight())/2+fm.getAscent();

            x.drawString(s,xx,yy);
            x.dispose();
        }
    }

    // ---------------- COMMON UI ----------------

    JPanel page(){
        JPanel p=new JPanel(new BorderLayout(0,20));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(25,30,25,30));
        return p;
    }

    JLabel label(String s,int size,boolean bold){
        JLabel l=new JLabel(s);
        l.setFont(new Font(
                "Segoe UI",
                bold?Font.BOLD:Font.PLAIN,
                size));
        l.setForeground(TEXT);
        return l;
    }

    JTextField field(){
        JTextField f=new JTextField();

        f.setFont(new Font(
                "Segoe UI",Font.PLAIN,15));

        f.setPreferredSize(
                new Dimension(300,42));

        f.setBorder(new CompoundBorder(
                new LineBorder(BORDER),
                new EmptyBorder(5,10,5,10)));

        return f;
    }

    void combo(JComboBox<String> c){
        c.setFont(new Font(
                "Segoe UI",Font.PLAIN,14));
        c.setPreferredSize(
                new Dimension(300,42));
    }

    // ---------------- CUSTOM BLUE BUTTON ----------------

    class MyButton extends JButton{

        MyButton(String text,int w,int h){
            super(text);

            setFont(new Font(
                    "Segoe UI",Font.BOLD,13));

            setForeground(Color.WHITE);

            setPreferredSize(new Dimension(w,h));
            setMinimumSize(new Dimension(w,h));
            setMaximumSize(new Dimension(w,h));

            setBorderPainted(false);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);

            setCursor(new Cursor(
                    Cursor.HAND_CURSOR));
        }

        protected void paintComponent(Graphics g){
            Graphics2D x=(Graphics2D)g.create();

            x.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Color c=BLUE;

            if(getModel().isPressed())
                c=BLUE.darker();
            else if(getModel().isRollover())
                c=BLUE.brighter();

            x.setColor(c);

            x.fillRoundRect(
                    0,0,getWidth()-1,getHeight()-1,
                    10,10);

            x.setColor(Color.WHITE);
            x.setFont(getFont());

            FontMetrics fm=x.getFontMetrics();
            String s=getText();

            int xx=(getWidth()-fm.stringWidth(s))/2;
            int yy=(getHeight()-fm.getHeight())/2
                    +fm.getAscent();

            x.drawString(s,xx,yy);
            x.dispose();
        }
    }

    // ---------------- STORAGE ----------------

    void save(){
        try{
            ObjectOutputStream o=new ObjectOutputStream(
                    new FileOutputStream(FILE));

            o.writeObject(records);
            o.writeInt(next);
            o.close();

        }catch(Exception e){
            JOptionPane.showMessageDialog(
                    this,"Unable to save data.");
        }
    }

    @SuppressWarnings("unchecked")
    void load(){
        File f=new File(FILE);
        if(!f.exists()) return;

        try{
            ObjectInputStream o=new ObjectInputStream(
                    new FileInputStream(f));

            records=(ArrayList<Record>)o.readObject();
            next=o.readInt();
            o.close();

        }catch(Exception e){
            records=new ArrayList<>();
            next=1001;
        }
    }

    // ---------------- NAVIGATION ----------------

    void show(String p){
        cl.show(pages,p);
        update();
    }

    // ---------------- RECORD ----------------

    static class Record implements Serializable{

        private static final long serialVersionUID=1L;

        int id;
        String student="",roll="",branch="",component="";
        String issue="",returnDate="",condition="",status="";
    }

    // ---------------- MAIN ----------------

    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            ElectronicComponentSystem app=
                    new ElectronicComponentSystem();
            app.setVisible(true);
        });
    }
}