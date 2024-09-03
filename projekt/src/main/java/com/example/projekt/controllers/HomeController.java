package com.example.projekt.controllers;

import com.example.projekt.models.*;
import com.example.projekt.scheduled.StatusStrony;
import com.example.projekt.services.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private HistoriaService historiaService;


    @Autowired
    private LokataService lokataService;

    @Autowired
    private LokataAktywnaService lokataAktywnaService;

    @Autowired
    private UserService userService;

    @Autowired
    private WalutaKupionaService walutaKupionaService;

    @Autowired
    private KursyService kursyService;

    @Autowired
    private KryptoService kryptoService;

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        Collection auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();;
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        String rola = auth.toString();
        if(rola.equals("[ROLE_USER]")){
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            modelAndView = new ModelAndView("index_user");
            modelAndView.addObject("name", name);
        }
        return modelAndView;
    }

    @GetMapping("/lokaty")
    public ModelAndView lokaty_index(LokataAktywnaDto lokataAktywna) {
        StatusStrony statusStrony = StatusStrony.getInstance();
        if(statusStrony.getMaintenece() || statusStrony.getMainteneceAdmin()){
            return new ModelAndView("maintenance");
        }
        ModelAndView modelAndView = new ModelAndView("lokaty");

        Collection auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();;
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        String rola = auth.toString();
        if(rola.equals("[ROLE_USER]")){
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            modelAndView = new ModelAndView("lokaty_user");
            modelAndView.addObject("name", name);
        }

        modelAndView.addObject("lokataItems", lokataService.getAll());
        return modelAndView;
    }

    @PostMapping("/dodajLokate")
    public String dodajLokate(@Valid LokataAktywnaDto lokataAktywnaDto, Model model) {

        //System.out.println(lokataAktywna.getId());
        //System.out.println(lokataAktywna.getIlosc());
        LokataAktywna la= new LokataAktywna();
        if (lokataAktywnaDto.getId()==null) {
            return "redirect:/lokaty?nieWybrano";
        }

        Lokata l = lokataService.getById(lokataAktywnaDto.getId()).get();
        if (l.getMinimum()>lokataAktywnaDto.getIlosc()){
            return "redirect:/lokaty?zaMalo";
        }
        Long max;
        if(l.getMaksimum()==0){
            max = Long.MAX_VALUE;
        } else{
            max = l.getMaksimum();
        }
        if (max<lokataAktywnaDto.getIlosc()){
            return "redirect:/lokaty?zaDuzo";
        }

        la.setBank(l.getBank()); //cos nie dziala
        la.setIlosc(lokataAktywnaDto.getIlosc());
        la.setNazwa(l.getNazwa());
        la.setProcent(l.getProcent());
        la.setProcent_po_opodatkowaniu(l.getProcent_po_opodatkowaniu());
        la.setAnulowanie(l.getAnulowanie());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String zalogowanyLogin = authentication.getName();
        User user = userService.getUserByUsername(zalogowanyLogin);
        la.setUser_id(user);

        Calendar cal = Calendar.getInstance();
        Timestamp ts2 = new Timestamp(System.currentTimeMillis());
        cal.setTime(ts2);
        la.setData_start(Date.valueOf(String.valueOf(ts2.toLocalDateTime().toLocalDate())));

        cal.add(Calendar.MONTH, l.getOkresInt());
        ts2.setTime(cal.getTime().getTime());
        la.setData_koniec(Date.valueOf(String.valueOf(ts2.toLocalDateTime().toLocalDate())));

        lokataAktywnaService.save(la);
        return "redirect:/lokaty?dodano";
    }

    @PostMapping("/dodajLokateWlasna")
    public String dodajLokateWlasna(@Valid LokataAktywnaDto lokataAktywnaDto, Model model) {

        //System.out.println(lokataAktywna.getId());
        //System.out.println(lokataAktywna.getIlosc());
        LokataAktywna la= new LokataAktywna();
        if(Objects.equals(lokataAktywnaDto.getBank(), "") ||
        lokataAktywnaDto.getAnulowanie().equals("") ||
        lokataAktywnaDto.getNazwa().equals("") ||
        lokataAktywnaDto.getIlosc()==null ||
        lokataAktywnaDto.getOkres()==null ||
        lokataAktywnaDto.getProcent()==null ||
        lokataAktywnaDto.getProcent_po_opodatkowaniu()==null){
            return "redirect:/lokaty?wlasneBlad";
        }


        la.setBank(lokataAktywnaDto.getBank()); //cos nie dziala
        la.setIlosc(lokataAktywnaDto.getIlosc());
        la.setNazwa(lokataAktywnaDto.getNazwa());
        la.setProcent(lokataAktywnaDto.getProcent());
        la.setProcent_po_opodatkowaniu(lokataAktywnaDto.getProcent_po_opodatkowaniu());
        la.setAnulowanie(lokataAktywnaDto.getAnulowanie());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String zalogowanyLogin = authentication.getName();
        User user = userService.getUserByUsername(zalogowanyLogin);
        la.setUser_id(user);

        Calendar cal = Calendar.getInstance();
        Timestamp ts2 = new Timestamp(System.currentTimeMillis());
        cal.setTime(ts2);
        la.setData_start(Date.valueOf(String.valueOf(ts2.toLocalDateTime().toLocalDate())));

        cal.add(Calendar.MONTH, lokataAktywnaDto.getOkresInt());
        ts2.setTime(cal.getTime().getTime());
        la.setData_koniec(Date.valueOf(String.valueOf(ts2.toLocalDateTime().toLocalDate())));

        lokataAktywnaService.save(la);
        return "redirect:/lokaty?dodano";
    }

    @GetMapping("/oszczednosci")
    public ModelAndView oszczednosci() {
        StatusStrony statusStrony = StatusStrony.getInstance();
        if(statusStrony.getMaintenece() || statusStrony.getMainteneceAdmin()){
            return new ModelAndView("maintenance");
        }
        ModelAndView modelAndView = new ModelAndView("oszczednosci");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String zalogowanyLogin = authentication.getName();
        User user = userService.getUserByUsername(zalogowanyLogin);

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        modelAndView.addObject("name", name);

        Iterator<LokataAktywna> lokatyAkt = lokataAktywnaService.getAll().iterator();
        System.out.println("1");
        Set<LokataAktywna> lokatyUser = new HashSet<>();
        BigDecimal lokatyWartosc = new BigDecimal(0);
        while (lokatyAkt.hasNext()){
            System.out.println("2");
            LokataAktywna z = lokatyAkt.next();
            if(user.getId().equals(z.getUser_id().getId())){
                System.out.println("3");
                lokatyUser.add(z);
                lokatyWartosc = lokatyWartosc.add(BigDecimal.valueOf(z.getIlosc()));
            }
        }
        System.out.println("4");

        List<WalutaKupiona> walutaKupionaList = walutaKupionaService.getAll();

        List<WalutaKupiona> walutyUser = new LinkedList<>();
        List<WalutaKupiona> kryptoUser = new LinkedList<>();
        BigDecimal walutyWartosc = new BigDecimal(0);
        BigDecimal kryptoWartosc = new BigDecimal(0);
        BigDecimal kursWartosc = new BigDecimal(0);

        Kurs kurs;
        for(WalutaKupiona w:walutaKupionaList) {
            if(w.getUser_id().getId().equals(user.getId())) {
                if(w.getIlosc().floatValue()!=0) {
                    if(!w.getCzy_krypto()) {
                        walutyUser.add(w);
                        switch (w.getNazwa()){
                            case "USD":
                                kurs = kursyService.getUSDNewest().get();
                                kursWartosc = kurs.getKupno().add(kurs.getSprzedarz()).divide(new BigDecimal(2));
                                break;
                            case "GBP":
                                kurs = kursyService.getGBPNewest().get();
                                kursWartosc = kurs.getKupno().add(kurs.getSprzedarz()).divide(new BigDecimal(2));
                                break;
                            case "CHF":
                                kurs = kursyService.getCHFNewest().get();
                                kursWartosc = kurs.getKupno().add(kurs.getSprzedarz()).divide(new BigDecimal(2));
                                break;
                            case "EUR":
                                kurs = kursyService.getEURNewest().get();
                                kursWartosc = kurs.getKupno().add(kurs.getSprzedarz()).divide(new BigDecimal(2));
                                break;
                        }
                        walutyWartosc = walutyWartosc.add(w.getIlosc().multiply(kursWartosc));
                    } else{
                        kryptoUser.add(w);
                        switch (w.getNazwa()){
                            case "Bitcoin":
                                KursBTC kursBTC = kryptoService.getBTCNewest().get();
                                kursWartosc = kursBTC.getKurs();
                                break;
                            case "Ethereum":
                                KursETH kursETH = kryptoService.getETHNewest().get();
                                kursWartosc = kursETH.getKurs();
                                break;
                            case "Euro-coin":
                                KursEUC kursEUC = kryptoService.getEUCNewest().get();
                                kursWartosc = kursEUC.getKurs();
                                break;
                            case "Tether":
                                KursTET kursTET = kryptoService.getTETNewest().get();
                                kursWartosc = kursTET.getKurs();
                                break;
                            case "Tether-gold":
                                KursTEG kursTEG = kryptoService.getTEGNewest().get();
                                kursWartosc = kursTEG.getKurs();
                                break;
                            case "Usd-coin":
                                KursUSC kursUSC = kryptoService.getUSCNewest().get();
                                kursWartosc = kursUSC.getKurs();
                                break;
                        }
                        kryptoWartosc = kryptoWartosc.add(w.getIlosc().multiply(kursWartosc));
                    }
                }
            }
        }

        System.out.println(lokatyWartosc);
        System.out.println(walutyWartosc);
        System.out.println(kryptoWartosc);

        modelAndView.addObject("lokatyWartosc", lokatyWartosc);
        modelAndView.addObject("walutyWartosc", walutyWartosc);
        modelAndView.addObject("kryptoWartosc", kryptoWartosc);

        modelAndView.addObject("lokataAktywnaItems", lokatyUser);
        modelAndView.addObject("walutaItems", walutyUser);
        modelAndView.addObject("kryptoItems", kryptoUser);
        return modelAndView;
    }

    @GetMapping("/zakonczLokate/{id}")
    public String zakonczLokate(@PathVariable("id") Integer id, Model model) {
        LokataAktywna lokataAktywna = lokataAktywnaService.getById(Long.valueOf(id)).get();
        //dodać do historii
        Historia historia = new Historia();
        Calendar cal = Calendar.getInstance();
        Timestamp ts2 = new Timestamp(System.currentTimeMillis());
        cal.setTime(ts2);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String zalogowanyLogin = authentication.getName();
        User user = userService.getUserByUsername(zalogowanyLogin);
        historia.setUser_id(user);

        historia.setNazwa(lokataAktywna.getBank()+" - "+lokataAktywna.getNazwa());
        historia.setTyp("Lokata");
        historia.setWklad(BigDecimal.valueOf(lokataAktywna.getIlosc()));
        historia.setData_koniec(Date.valueOf(String.valueOf(ts2.toLocalDateTime().toLocalDate())));
        historia.setData_start(lokataAktywna.getData_start());
        if(Date.valueOf(String.valueOf(ts2.toLocalDateTime().toLocalDate())).equals(lokataAktywna.getData_koniec())){
            Calendar start = Calendar.getInstance();
            start.setTime(lokataAktywna.getData_start());

            int startDzienMies = start.get(Calendar.DAY_OF_MONTH);
            int startMies = 12 * start.get(Calendar.YEAR)+ start.get(Calendar.MONTH);

            Calendar koniec = Calendar.getInstance();
            koniec.setTime(lokataAktywna.getData_koniec());

            int koniecDzienMies= koniec.get(Calendar.DAY_OF_MONTH);
            int koniecMies= 12 * koniec.get(Calendar.YEAR)+ koniec.get(Calendar.MONTH);
            int okres;
            if(startDzienMies > koniecDzienMies){
                okres = koniecMies - startMies -1;
            }
            else {
                okres = koniecMies-startMies;
            }
            historia.setZwrot(BigDecimal.valueOf((float)lokataAktywna.getIlosc()+
                    (((float)lokataAktywna.getIlosc()
                    *((lokataAktywna.getProcent().floatValue()/100)/12))
                    *(float)okres)));
        }else{
            if(lokataAktywna.getAnulowanie().equals("Utrata całości odsetek")){
                historia.setZwrot(BigDecimal.valueOf(lokataAktywna.getIlosc()));
            }else if(lokataAktywna.getAnulowanie().equals("Oprocentowanie obniżone do 0,1%")){
                Calendar start = Calendar.getInstance();
                start.setTime(lokataAktywna.getData_start());

                int startDzienMies = start.get(Calendar.DAY_OF_MONTH);
                int startMies = 12 * start.get(Calendar.YEAR) + start.get(Calendar.MONTH);

                Calendar koniec = Calendar.getInstance();
                koniec.setTime(Date.valueOf(String.valueOf(ts2.toLocalDateTime().toLocalDate())));

                int koniecDzienMies = koniec.get(Calendar.DAY_OF_MONTH);
                int koniecMies = 12 * koniec.get(Calendar.YEAR) + koniec.get(Calendar.MONTH);
                int okres;
                if (startDzienMies > koniecDzienMies) {
                    okres = koniecMies - startMies - 1;
                } else {
                    okres = koniecMies - startMies;
                }
                historia.setZwrot(BigDecimal.valueOf(lokataAktywna.getIlosc()+
                        ((float)lokataAktywna.getIlosc()
                                *((float)0.1/12)
                                *(float)okres)));
            }
        }

        historiaService.save(historia);
        lokataAktywnaService.delete(lokataAktywna);
        return "redirect:/oszczednosci?zakonczono";
    }

    @GetMapping("/historia")
    public ModelAndView historia() {
        StatusStrony statusStrony = StatusStrony.getInstance();
        if(statusStrony.getMaintenece() || statusStrony.getMainteneceAdmin()){
            return new ModelAndView("maintenance");
        }
        ModelAndView modelAndView = new ModelAndView("historia");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String zalogowanyLogin = authentication.getName();
        User user = userService.getUserByUsername(zalogowanyLogin);

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        modelAndView.addObject("name", name);

        Iterator<Historia> historiaList = historiaService.getAll().iterator();
        List<Historia> historiaUser = new LinkedList<>();

        while (historiaList.hasNext()){
            Historia z = historiaList.next();
            if(user.getId().equals(z.getUser_id().getId())){
                historiaUser.add(z);
            }
        }
        Collections.reverse(historiaUser);
        //zakładając że wszystkie nowe wiersze są wstawiane na dół tabeli, odwracjąc listę mamy ją posortowaną od najnowszych
        modelAndView.addObject("historiaItems", historiaUser);
        return modelAndView;
    }

    @GetMapping("/login")
    public String logowanie(){
        StatusStrony statusStrony = StatusStrony.getInstance();
        return "login";
    }


    @GetMapping("/logout")
    public String logout() {
        StatusStrony statusStrony = StatusStrony.getInstance();
        if(statusStrony.getMaintenece() || statusStrony.getMainteneceAdmin()){
            return "maintenance";
        }
        return "redirect:/login";
    }

    @GetMapping("/opisEmerytury")
    public ModelAndView opisEmerytury() {
        ModelAndView modelAndView = new ModelAndView("opisEmerytury");
        Collection auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();;
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        String rola = auth.toString();
        if(rola.equals("[ROLE_USER]")){
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            modelAndView = new ModelAndView("opisEmerytury_user");
            modelAndView.addObject("name", name);
        }
        return modelAndView;
    }

    @GetMapping("/opisLokaty")
    public ModelAndView opisLokaty() {
        ModelAndView modelAndView = new ModelAndView("opisLokaty");
        Collection auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();;
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        String rola = auth.toString();
        if(rola.equals("[ROLE_USER]")){
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            modelAndView = new ModelAndView("opisLokaty_user");
            modelAndView.addObject("name", name);
        }
        return modelAndView;
    }

    @GetMapping("/opisWaluty")
    public ModelAndView opisWaluty() {
        ModelAndView modelAndView = new ModelAndView("opisWaluty");
        Collection auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();;
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        String rola = auth.toString();
        if(rola.equals("[ROLE_USER]")){
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            modelAndView = new ModelAndView("opisWaluty_user");
            modelAndView.addObject("name", name);
        }
        return modelAndView;
    }

    @GetMapping("/opisKrypto")
    public ModelAndView opisKrypto() {
        ModelAndView modelAndView = new ModelAndView("opisKrypto");
        Collection auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();;
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        String rola = auth.toString();
        if(rola.equals("[ROLE_USER]")){
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            modelAndView = new ModelAndView("opisKrypto_user");
            modelAndView.addObject("name", name);
        }
        return modelAndView;
    }

    @GetMapping("/opisFunduszeInwestycyjne")
    public ModelAndView opisFunduszeInwestycyjne() {
        ModelAndView modelAndView = new ModelAndView("opisFunduszeInwestycyjne");
        Collection auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities();;
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        String rola = auth.toString();
        if(rola.equals("[ROLE_USER]")){
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            modelAndView = new ModelAndView("opisFunduszeInwestycyjne_user");
            modelAndView.addObject("name", name);
        }
        return modelAndView;
    }
}
