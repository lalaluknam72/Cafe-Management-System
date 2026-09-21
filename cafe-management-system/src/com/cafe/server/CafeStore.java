package com.cafe.server;

import com.cafe.model.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class CafeStore {

    public static final CafeStore INSTANCE = new CafeStore();

    public final Map<Integer, Product> products = new ConcurrentHashMap<>();
    public final Map<Integer, Order> orders = new ConcurrentHashMap<>();
    public final Map<Integer, Table> tables = new ConcurrentHashMap<>();
    public final Map<Integer, Reservation> reservations = new ConcurrentHashMap<>();
    public final Inventory inventory = new Inventory();
    public final List<Income> incomes = new CopyOnWriteArrayList<>();
    public final List<Expense> expenses = new CopyOnWriteArrayList<>();

    //ตัวนับ id อัตโนมัติของแต่ละประเภทข้อมูล
    private final AtomicInteger productIdSeq = new AtomicInteger(1);
    private final AtomicInteger orderIdSeq = new AtomicInteger(1);
    private final AtomicInteger tableIdSeq = new AtomicInteger(1);
    private final AtomicInteger reservationIdSeq = new AtomicInteger(1);
    private final AtomicInteger ingredientIdSeq = new AtomicInteger(1);
    private final AtomicInteger paymentIdSeq = new AtomicInteger(1);
    private final AtomicInteger incomeIdSeq = new AtomicInteger(1);
    private final AtomicInteger expenseIdSeq = new AtomicInteger(1);

    private CafeStore() {
        seedData();
    }

    public int nextProductId() { return productIdSeq.getAndIncrement(); }
    public int nextOrderId() { return orderIdSeq.getAndIncrement(); }
    public int nextTableId() { return tableIdSeq.getAndIncrement(); }
    public int nextReservationId() { return reservationIdSeq.getAndIncrement(); }
    public int nextIngredientId() { return ingredientIdSeq.getAndIncrement(); }
    public int nextPaymentId() { return paymentIdSeq.getAndIncrement(); }
    public int nextIncomeId() { return incomeIdSeq.getAndIncrement(); }
    public int nextExpenseId() { return expenseIdSeq.getAndIncrement(); }

    private void seedData() {
        //COFFEE (กาแฟ)
        addProduct("เอสเพรสโซ่ (HOT)", "COFFEE", 30.0, "เอสเพรสโซ่ร้อน", "https://www.nespresso.com/ecom/medias/sys_master/public/46591974866974/shutterstock-2524508273-1200x800.jpg", true);
        addProduct("เอสเพรสโซ่ (ICED)", "COFFEE", 40.0, "เอสเพรสโซ่เย็น", "https://img.wongnai.com/p/1920x0/2021/09/23/49bc4c3b4fca45eaae9cfdc53c1c945a.jpg", true);
        addProduct("เอสเพรสโซ่ (FRAPPE)", "COFFEE", 45.0, "เอสเพรสโซ่ปั่น", "https://cdn.shopify.com/s/files/1/0778/0591/2351/files/benefit_coffee_frappe_1024x1024.jpg?v=1725614702", true);
        addProduct("เอสเพรสโซ่ เฮเซลนัท (ICED)", "COFFEE", 45.0, "เอสเพรสโซ่ เฮเซลนัท เย็น", "https://www.nestleprofessional.co.th/sites/default/files/styles/np_recipe_detail/public/2022-04/nescafe-iced-espresso-540x400.jpg?itok=cGl4pcYu", true);
        addProduct("คาปูชิโน่ (HOT)", "COFFEE", 30.0, "คาปูชิโน่ร้อน", "https://www.nespresso.com/ecom/medias/sys_master/public/45936680796190/shutterstock-1074209732-1024x683.jpg", true);
        addProduct("คาปูชิโน่ (ICED)", "COFFEE", 40.0, "คาปูชิโน่เย็น", "https://www.nestleprofessional.co.th/sites/default/files/styles/np_recipe_detail/public/2023-04/BANNER_%E0%B8%84%E0%B8%B2%E0%B8%9B%E0%B8%B9%E0%B8%8A%E0%B8%B4%E0%B9%82%E0%B8%99%E0%B9%88%20%E0%B9%80%E0%B8%A2%E0%B9%87%E0%B8%99%20540x400%20px.jpg?itok=_jPfaN4n", true);
        addProduct("คาปูชิโน่ (FRAPPE)", "COFFEE", 45.0, "คาปูชิโน่ปั่น", "https://cdn.shopify.com/s/files/1/0778/0591/2351/files/benefit_coffee_frappe_1024x1024.jpg?v=1725614702", true);
        addProduct("มอคค่า (HOT)", "COFFEE", 35.0, "มอคค่าร้อน", "https://www.nespresso.com/ecom/medias/sys_master/public/46619868856350/shutterstock-1138068680-1200x800.jpg", true);
        addProduct("มอคค่า (ICED)", "COFFEE", 45.0, "มอคค่าเย็น", "https://www.falconforprofessional.com/wp-content/uploads/2023/09/82.jpg", true);
        addProduct("มอคค่า (FRAPPE)", "COFFEE", 50.0, "มอคค่าปั่น", "https://s359.kapook.com/pagebuilder/9c4374c3-7e6a-4d09-8186-f5a64c6eca66.jpg", true);
        addProduct("มัคคิอาโต (ICED)", "COFFEE", 50.0, "มัคคิอาโตเย็น", "https://www.nestleprofessional.co.th/sites/default/files/styles/np_recipe_detail/public/2022-04/nescafe-iced-caramel-macchiato-540x400%20%281%29.jpg?itok=u3jWdrDl", true);
        addProduct("ลาเต้ (HOT)", "COFFEE", 30.0, "ลาเต้ร้อน", "https://s359.kapook.com/pagebuilder/937cadcd-2fed-4cca-a111-8bad7fbd54f8.jpg", true);
        addProduct("ลาเต้ (ICED)", "COFFEE", 40.0, "ลาเต้เย็น", "https://www.nespresso.com/ecom/medias/sys_master/public/47204036706334/shutterstock-2170419379-1024x683.jpg", true);
        addProduct("ลาเต้ (FRAPPE)", "COFFEE", 45.0, "ลาเต้ปั่น", "https://cdn.shopify.com/s/files/1/0778/0591/2351/files/coffee_frappe_at_home_1024x1024.jpg?v=1725614744", true);
        addProduct("อเมริกาโน่ (HOT)", "COFFEE", 30.0, "อเมริกาโน่ร้อน", "https://www.nespresso.com/ecom/medias/sys_master/public/46579643580446/shutterstock-2464831389-1024x683.jpg", true);
        addProduct("อเมริกาโน่ (ICED)", "COFFEE", 40.0, "อเมริกาโน่เย็น", "https://www.nespresso.com/ecom/medias/sys_master/public/45912205852702/shutterstock-2263751173-1024x683.jpg", true);
        addProduct("อเมริกาโน่ มะนาว/ส้ม (ICED)", "COFFEE", 40.0, "อเมริกาโน่ส้มเย็น", "https://s359.kapook.com/pagebuilder/7b0ae0d8-1e23-4451-a68c-242a05ee7598.jpg", true);

        //TEA (ชา)
        addProduct("ชาเขียว (HOT)", "TEA", 30.0, "ชาเขียวร้อน", "https://chobreview.com/wp-content/uploads/2021/11/%E0%B8%9C%E0%B8%87%E0%B8%8A%E0%B8%B2%E0%B9%80%E0%B8%82%E0%B8%B5%E0%B8%A2%E0%B8%A7-%E0%B8%A2%E0%B8%B5%E0%B9%88%E0%B8%AB%E0%B9%89%E0%B8%AD%E0%B9%84%E0%B8%AB%E0%B8%99%E0%B8%94%E0%B8%B5-%E0%B8%A1%E0%B8%B5%E0%B8%9B%E0%B8%A3%E0%B8%B0%E0%B9%82%E0%B8%A2%E0%B8%8A%E0%B8%99%E0%B9%8C.jpg", true);
        addProduct("ชาเขียว (ICED)", "TEA", 35.0, "ชาเขียวเย็น", "https://s.isanook.com/wo/0/ud/37/188993/4.jpg?ip/resize/w728/q80/jpg", true);
        addProduct("ชาเขียว (FRAPPE)", "TEA", 40.0, "ชาเขียวปั่น", "https://www.pholfoodmafia.com/wp-content/uploads/2019/03/Matcha-Frappe-big.jpg", true);
        addProduct("ชาเขียวมะนาว (HOT)", "TEA", 30.0, "ชาเขียวมะนาวร้อน", "https://png.pngtree.com/png-vector/20210429/ourlarge/pngtree-delicious-lemon-green-tea-png-image_3240508.png", true);
        addProduct("ชาเขียวมะนาว (ICED)", "TEA", 35.0, "ชาเขียวมะนาวเย็น", "https://api2.krua.co/wp-content/uploads/2020/07/RD0200_Gallery_-01-scaled.jpg", true);
        addProduct("ชาไทย (HOT)", "TEA", 30.0, "ชาไทยร้อน", "https://st-th-1.byteark.com/assets.punpro.com/contents/i19060/1631270057450-shutterstock_1337117723.jpg", true);
        addProduct("ชาไทย (ICED)", "TEA", 35.0, "ชาไทยเย็น", "https://benothailand.com/cdn/shop/articles/Iced_thai_milk_tea_in_glass_9c10e268-b2bd-4406-a25f-e27d39f03d92.jpg?v=1780021592", true);
        addProduct("ชาไทย (FRAPPE)", "TEA", 40.0, "ชาไทยปั่น", "https://www.fnthaidairies.com/public/uploads/recipe_management/images/abx2XSWTmsdog5ztWmhjh1MTGIbON32o4YcaoRvUGh9SkGJFaH1611740897.jpg", true);
        addProduct("ชาแอปเปิ้ล (HOT)", "TEA", 30.0, "ชาแอปเปิ้ลร้อน", "https://medthai.com/wp-content/uploads/2013/07/Apple-Cider-1.jpg", true);
        addProduct("ชาแอปเปิ้ล (ICED)", "TEA", 35.0, "ชาแอปเปิ้ลเย็น", "https://www.nestleprofessional.co.th/sites/default/files/styles/np_recipe_detail/public/2022-04/nestea-iced-apple-540x400.jpg?itok=LVelxLVX", true);
        addProduct("ชาน้ำผึ้งมะนาว (HOT)", "TEA", 30.0, "ชาน้ำผึ้งมะนาวร้อน", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTOxn8jrM64wgTx00Q1ahsdlQKBrZp5IphgkMLFCzvyVn_9LWh_vnRTPniM&s=10", true);
        addProduct("ชาน้ำผึ้งมะนาว (ICED)", "TEA", 35.0, "ชาน้ำผึ้งมะนาวเย็น", "https://img.kapook.com/u/2015/surauch/cook2/e1.jpg", true);
        addProduct("ชาพีช (HOT)", "TEA", 30.0, "ชาพีชร้อน", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQuRz1RXJWfLO1zjjXH4ptQU1M_py2Ikse_zJS-DJ2gN8NAMiRLdzGs9mnO&s=10", true);
        addProduct("ชาพีช (ICED)", "TEA", 35.0, "ชาพีชเย็น", "https://smoosh.me/wp-content/uploads/2024/11/%E0%B8%8A%E0%B8%B2%E0%B8%9E%E0%B8%B5%E0%B8%8A%E0%B8%9E%E0%B8%A3%E0%B9%89%E0%B8%AD%E0%B8%A1%E0%B9%80%E0%B8%99%E0%B8%B7%E0%B9%89%E0%B8%AD%E0%B8%9E%E0%B8%B5%E0%B8%8A-1024x768.webp", true);
        addProduct("ชามะนาว (HOT)", "TEA", 30.0, "ชามะนาวร้อน", "https://img.lazcdn.com/g/p/77984c133734fcf8dcc6400e76fd8b45.jpg_960x960q80.jpg_.webp", true);
        addProduct("ชามะนาว (ICED)", "TEA", 35.0, "ชามะนาวเย็น", "https://inwfile.com/s-dz/qpu5eh.jpg", true);
        addProduct("น้ำผึ้งมะนาว (HOT)", "TEA", 30.0, "น้ำผึ้งมะนาวร้อน", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTcKg0Q9m6NNTb5HIrpfnOC0VRoY3jHr035HZSabhQgvdTR4JvLNqxcpYs&s=10", true);
        addProduct("น้ำผึ้งมะนาว (ICED)", "TEA", 35.0, "น้ำผึ้งมะนาวเย็น", "https://smoosh.me/wp-content/uploads/2024/11/%E0%B8%99%E0%B9%89%E0%B8%B3%E0%B8%9C%E0%B8%B6%E0%B9%89%E0%B8%87%E0%B8%A1%E0%B8%B0%E0%B8%99%E0%B8%B2%E0%B8%A7.webp", true);
        addProduct("มัทฉะลาเต้ (ICED)", "TEA", 50.0, "มัทฉะลาเต้เย็น", "https://s359.kapook.com/pagebuilder/0bc528d9-941e-450f-8193-6ac7242a85d8.jpg", true);

        //COCOA & CHOCOLATE (โกโก้ & ช็อคโกแลต)
        addProduct("โกโก้ (HOT)", "COCOA & CHOCOLATE", 30.0, "โกโก้ร้อน", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQw5Dr-CTJ_FaCXwflQ8ly5HJPUR4646rXyBcOXwmE9yVe5cxA3x5YWOmI&s=10", true);
        addProduct("โกโก้ (ICED)", "COCOA & CHOCOLATE", 35.0, "โกโก้เย็น", "https://www.falconforprofessional.com/wp-content/uploads/2023/10/91.jpg", true);
        addProduct("โกโก้ (FRAPPE)", "COCOA & CHOCOLATE", 40.0, "โกโก้ปั่น", "https://s3.ap-southeast-1.amazonaws.com/lbx-snp-cms/images/menu/4740-qul3CYVpY91736489045.webp", true);
        addProduct("โกโก้ไวท์มอลต์ (FRAPPE)", "COCOA & CHOCOLATE", 45.0, "โกโก้ไวท์มอลต์ปั่น", "https://www.bluemochatea.com/wp-content/uploads/2019/09/c9c15f2e2ae34f4ebe7eb16f4efecb63.jpg", true);
        addProduct("โอวัลติน (HOT)", "COCOA & CHOCOLATE", 30.0, "โอวัลตินร้อน", "https://www.shutterstock.com/image-photo/hot-ovaltine-glass-coconut-chocolate-260nw-2480760409.jpg", true);
        addProduct("โอวัลติน (ICED)", "COCOA & CHOCOLATE", 35.0, "โอวัลตินเย็น", "https://img.kapook.com/u/2017/wanwanat/75_milo/milo1.jpg", true);
        addProduct("โอวัลติน (FRAPPE)", "COCOA & CHOCOLATE", 40.0, "โอวัลตินปั่น", "https://api2.krua.co/wp-content/uploads/2020/09/RD0193_ImageBannerMobile_960x633_New_-01-scaled.jpg", true);
        addProduct("ช็อคโกแล็ต (HOT)", "COCOA & CHOCOLATE", 30.0, "ช็อคโกแล็ตร้อน", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQeQei3E4Itv2VQrisOmEO_eCGOdqoGulP9Q9kNUuOxrCTOQCYOqN_Fyc8&s=10", true);
        addProduct("ช็อคโกแล็ต (ICED)", "COCOA & CHOCOLATE", 35.0, "ช็อคโกแล็ตเย็น", "https://www.nestleprofessional.co.th/sites/default/files/styles/np_recipe_detail/public/2022-04/nestle-chocolate-overload-540x40.png?itok=vIvTjZDB", true);
        addProduct("ช็อคโกแล็ต (FRAPPE)", "COCOA & CHOCOLATE", 40.0, "ช็อคโกแล็ตปั่น", "https://www.falconforprofessional.com/wp-content/uploads/2023/09/17_0.jpg", true);

        // --- MILK (นม) ---
        addProduct("นมคาราเมล (HOT)", "MILK", 30.0, "นมคาราเมลร้อน", "https://cdn.shopify.com/s/files/1/0778/0591/2351/files/hot_caramel_macchiato_c3f6b6e9-88c6-497e-afd1-4c412de9f454_1024x1024.jpg?v=1738157614", true);
        addProduct("นมคาราเมล (ICED)", "MILK", 40.0, "นมคาราเมลเย็น", "https://www.hatyaifocus.com/ckeditor/upload/forums/3/%E0%B8%81%E0%B8%B1%E0%B8%93%E0%B8%91%E0%B9%8C/108%20tea%20room/W%20108%20tea%20room-10.jpg", true);
        addProduct("นมคาราเมล (FRAPPE)", "MILK", 45.0, "นมคาราเมลปั่น", "https://www.nestleprofessional.co.th/sites/default/files/styles/np_wysiwyg_image_small/public/2023-03/BANNER_%E0%B9%82%E0%B8%84%E0%B9%82%E0%B8%84%E0%B9%88%E0%B8%99%E0%B8%B1%E0%B8%97%20%E0%B9%82%E0%B8%AD%E0%B9%80%E0%B8%A7%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%82%E0%B8%AB%E0%B8%A5%E0%B8%94%20540x400%20px.jpg?itok=bB755-3U", true);
        addProduct("นมชมพู (HOT)", "MILK", 30.0, "นมชมพูร้อน", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ7c5iKFgh78zagzL_UpMXDWFkkVShxSBKUMfcDz-UYDuLxurO0w1CqGtk4&s=10", true);
        addProduct("นมชมพู (ICED)", "MILK", 35.0, "นมชมพูเย็น", "https://smoosh.me/wp-content/uploads/2024/11/%E0%B8%99%E0%B8%A1%E0%B8%8A%E0%B8%A1%E0%B8%9E%E0%B8%B9.webp", true);
        addProduct("นมชมพู (FRAPPE)", "MILK", 40.0, "นมชมพูปั่น", "https://www.bluemochatea.com/wp-content/uploads/2019/04/%E0%B8%99%E0%B8%A1%E0%B8%8A%E0%B8%A1%E0%B8%9E%E0%B8%B9%E0%B8%9B%E0%B8%B1%E0%B9%88%E0%B8%99.jpg", true);
        addProduct("นมสด (HOT)", "MILK", 30.0, "นมสดร้อน", "https://st2.depositphotos.com/2889869/11560/i/450/depositphotos_115600154-stock-photo-hot-milk-on-white-table.jpg", true);
        addProduct("นมสด (ICED)", "MILK", 35.0, "นมสดเย็น", "https://obs-ect.line-scdn.net/r/ect/ect/cj0tN2s4M2ViNG1vaGVkbSZzPWpwNiZ0PW0mdT0xZnZjODJvaWszNmcwJmk9MA", true);
        addProduct("นมสด (FRAPPE)", "MILK", 40.0, "นมสดปั่น", "https://static.thairath.co.th/media/dFQROr7oWzulq5Fa6rPdE5Ubkvrh27EJcpORhoGXV1ul0XXvGAWwAKaqfWUpcof5thc.jpg", true);
        addProduct("นมสดโอรีโอ้ (FRAPPE)", "MILK", 45.0, "นมสดโอรีโอ้ปั่น", "https://www.bluemochatea.com/wp-content/uploads/2018/10/663x442-video-rahasia-bikin-oreo-frappuccino-ala-starbucks-151218p.jpg", true);
        addProduct("นมสดไวท์มอลต์ (FRAPPE)", "MILK", 45.0, "นมสดไวท์มอลต์ปั่น", "https://img.wongnai.com/p/400x0/2020/12/12/abf5892bb38a4cd8b0b3c4632d428baa.jpg", true);
        addProduct("นมสดน้ำผึ้ง (HOT)", "MILK", 30.0, "นมสดน้ำผึ้งร้อน", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRCbGVkRPN_IGWuS7IN2Q2dpDKF9IJ-HkQL28j32egv4zsI8cimU8_Zfo4&s=10", true);
        addProduct("นมสดน้ำผึ้ง (ICED)", "MILK", 40.0, "นมสดน้ำผึ้งเย็น", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRcP2EN_e-bzjGLwL_i97hS-NLyEBAow7-GSr4S_eLCce8GibKDwNMuysI&s=10", true);
        addProduct("นมสดน้ำผึ้ง (FRAPPE)", "MILK", 45.0, "นมสดน้ำผึ้งปั่น", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRDwp13SuHL0Zm-cQU9-jvhlLU5-0BmbnPJFlD2EvxuFH5qIE9R5RAKe7lh&s=10", true);
        addProduct("นมสดปีโป้ (FRAPPE)", "MILK", 45.0, "นมสดปีโป้ปั่น", "https://img.wongnai.com/p/1920x0/2019/06/15/7f9b170f206b4606bef65060ee049207.jpg", true);

        // --- SODA (โซดา) ---
        addProduct("แดงมะนาวโซดา", "SODA", 35.0, "แดงมะนาวโซดา", "https://img-global.cpcdn.com/recipes/5ab0f8b1dee89845/1200x630cq80/photo.jpg", true);
        addProduct("แอปเปิ้ลโซดา", "SODA", 35.0, "แอปเปิ้ลโซดา", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTzhBrjoy4KT-ArM-MABTQIkbMmZgMnG7bmy4zupWRYoQ&s=10", true);
        addProduct("กีวีโซดา", "SODA", 35.0, "กีวีโซดา", "https://app-cciyo.com/images/product/1/409_1.jpg", true);
        addProduct("น้ำผึ้งมะนาวโซดา", "SODA", 35.0, "น้ำผึ้งมะนาวโซดา", "https://img.magnific.com/premium-photo/honey-lemon-soda_1627593-983.jpg", true);
        addProduct("มะนาวโซดา", "SODA", 35.0, "มะนาวโซดา", "https://vdevs.versatilehaus.com/vdev9/images/products/gallery/beverage-citrus-close-up-1265910.jpg", true);
        addProduct("บลูพาราไดซ์โซดา", "SODA", 35.0, "บลูพาราไดซ์โซดา", "https://i.ytimg.com/vi/pjkYBgo-CQk/maxresdefault.jpg", true);
        addProduct("บลูเบอร์รี่โซดา", "SODA", 35.0, "บลูเบอร์รี่โซดา", "https://png.pngtree.com/background/20231013/original/pngtree-blueberry-italian-soda-lemon-white-splash-photo-picture-image_5494767.jpg", true);
        addProduct("ลิ้นจี่โซดา", "SODA", 35.0, "ลิ้นจี่โซดา", "https://img.wongnai.com/p/1920x0/2019/04/30/25b3d97278e740e0bd1db0693e8ae1bb.jpg", true);
        addProduct("พีชโซดา", "SODA", 35.0, "พีชโซดา", "https://img.wongnai.com/p/1920x0/2020/04/16/61739e13e0ae4930b8647b5303d698c7.jpg", true);
        addProduct("เขียวโซดา", "SODA", 35.0, "เขียวโซดา", "https://s.isanook.com/wo/0/ud/38/190829/d.jpg", true);

        // --- TOPPING (ท็อปปิ้ง) ---
        addProduct("เพิ่มวิปครีม", "TOPPING", 10.0, "วิปครีม", "https://tecnogasthai.com/wp-content/uploads/2023/09/1.-%E0%B8%A7%E0%B8%B4%E0%B8%9B%E0%B8%9B%E0%B8%B4%E0%B9%89%E0%B8%87%E0%B8%84%E0%B8%A3%E0%B8%B5%E0%B8%A1%E0%B9%81%E0%B8%A5%E0%B8%B0%E0%B8%A7%E0%B8%B4%E0%B8%9B%E0%B8%84%E0%B8%A3%E0%B8%B5%E0%B8%A1%E0%B9%80%E0%B8%AB%E0%B8%A1%E0%B8%B7%E0%B8%AD%E0%B8%99%E0%B8%81%E0%B8%B1%E0%B8%99%E0%B8%A1%E0%B8%B1%E0%B9%89%E0%B8%A2_.png", true);
        addProduct("เพิ่มมุกบุก", "TOPPING", 5.0, "มุกบุกบราวน์ชูการ์", "https://static.wixstatic.com/media/16e674_04161c1a980c43bcba16d1b5cfc88c5d.jpg/v1/fill/w_480,h_363,al_c,q_80,usm_0.66_1.00_0.01,enc_avif,quality_auto/16e674_04161c1a980c43bcba16d1b5cfc88c5d.jpg", true);

        // --- ข้อมูลโต๊ะ ---
        for (int i = 1; i <= 6; i++) {
            int id = nextTableId();
            tables.put(id, new Table(id, (i % 2 == 0) ? 4 : 2));
        }

        // --- ข้อมูลวัตถุดิบ ---
        inventory.addIngredient(new Ingredient(nextIngredientId(), "เมล็ดกาแฟ", "กรัม", 500, 200));
        inventory.addIngredient(new Ingredient(nextIngredientId(), "นมสด", "มล.", 1000, 300));
        inventory.addIngredient(new Ingredient(nextIngredientId(), "ผงชาเขียว", "กรัม", 150, 100));
    }

    private void addProduct(String name, String category, double price, String desc, String image, boolean available) {
        int id = nextProductId();
        products.put(id, new Product(id, name, category, price, desc, image, available));
    }
}
