package uas.deded.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uas.deded.model.TabunganModel;
import uas.deded.repository.TabunganRepository;

@Service
public class TabunganDao {
	@Autowired
	TabunganRepository tabunganRepository;
	
	//create
	public TabunganModel save(TabunganModel tabunganModel) {
		TabunganModel tab1 = tabunganModel;
		TabunganModel tab2 = tabunganRepository.getLastSaldo(tab1.getNik());
		if(tab2==null) {
			tab1.setSaldo(0 + tab1.getKredit() - tab1.getDebet());
			return tabunganRepository.save(tab1);
		}else {
			tab1.setSaldo(tab2.getSaldo() + tab1.getKredit() - tab1.getDebet());
			return tabunganRepository.save(tab1);
		}
	}
	
	//read all
	public List<TabunganModel> getAll(){
		return tabunganRepository.findAll();
	}
	
	//read by id
	public TabunganModel getDataById(Long id) {
		return tabunganRepository.findOne(id);
	}
	
	//read by nik
	public List<TabunganModel> getDataByNik(String nik){
		return tabunganRepository.getDataByNik(nik);
	}
	
	//update
	public TabunganModel update(TabunganModel tabunganModel) {
		TabunganModel tab1 = tabunganModel;
		TabunganModel tab2 = tabunganRepository.findOne(tab1.getId());
		tab2.setSaldo(tab2.getSaldo()+tab1.getKredit()-tab1.getDebet());
		tab2.setDebet(tab1.getDebet());
		tab2.setKredit(tab1.getKredit());
		Integer tamp = tab2.getSaldo();
		List<TabunganModel> tampp = tabunganRepository.getDataByNik(tab1.getNik());
		for(TabunganModel data : tampp) {
			if(data.getId() > tab1.getId()) {
				TabunganModel tab3 = tabunganRepository.findOne(data.getId());
				tab3.setSaldo(tamp + tab3.getKredit() - tab3.getDebet());
				tabunganRepository.save(tab3);
				tamp=tab3.getSaldo();
			}
		}
		return tabunganRepository.save(tab2);
	}
	
	// delete
	public void delete(long id) {
		TabunganModel tab1 = tabunganRepository.findOne(id);
		int tamp = tab1.getSaldo()-tab1.getKredit()+tab1.getDebet();
		List<TabunganModel> tampp=tabunganRepository.getDataByNik(tab1.getNik());
		for(TabunganModel data:tampp) {
			if(data.getId()>tab1.getId()) {
				TabunganModel tab2=tabunganRepository.findOne(data.getId());
				tab2.setSaldo(tamp+tab2.getKredit()-tab2.getDebet());
				tabunganRepository.save(tab2);
				tamp=tab2.getSaldo();
			}
		}
		tabunganRepository.delete(id);
	}
}
