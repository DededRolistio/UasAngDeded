package uas.deded.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uas.deded.dao.TabunganDao;
import uas.deded.model.TabunganModel;

@RestController
@RequestMapping("/bank")
public class TabunganController {
	@Autowired
	TabunganDao tabunganDao;
	
	//create
	@PostMapping("/simpan")
	public TabunganModel saveTabungan(@Valid @RequestBody TabunganModel tabunganModel) {
		return tabunganDao.save(tabunganModel);
	}

	//read all
	@GetMapping("/lihatSemua")
	public List<TabunganModel> getAll(){
		return tabunganDao.getAll();
	}

	//read by id
	@GetMapping("/lihatById/{id}")
	public ResponseEntity<TabunganModel> getDataById(@PathVariable(value="id")Long id){
		TabunganModel tab = tabunganDao.getDataById(id);
		if(tab == null) {
			return ResponseEntity.notFound().build();
		}else {
			return ResponseEntity.ok().body(tab);
		}
	}

	//read by nik
	@GetMapping("/lihatByNik/{nik}")
	public List<TabunganModel> getDataByNik(@PathVariable(value="nik")String nik){
		return tabunganDao.getDataByNik(nik);
	}

	//update
	@PutMapping("/ubah/{id}")
	public ResponseEntity<TabunganModel> update(@PathVariable(value="id")Long id, @Valid @RequestBody TabunganModel tabunganModel){
		TabunganModel tab1=tabunganDao.getDataById(id);
		if(tab1==null) {
			return ResponseEntity.notFound().build();
		}else {
			tab1.setSaldo(tab1.getSaldo()-tab1.getKredit()+tab1.getDebet());
			tab1.setDebet(tabunganModel.getDebet());
			tab1.setKredit(tabunganModel.getKredit());
			TabunganModel hasil=tabunganDao.update(tab1);
			return ResponseEntity.ok().body(hasil);
		}
	}

	//delete
	@DeleteMapping("/hapus/{id}")
	public ResponseEntity<TabunganModel> delet(@PathVariable(value="id")Long id){
		TabunganModel tab1 = tabunganDao.getDataById(id);
		if(tab1==null) {
			return ResponseEntity.notFound().build();
		}else {
			tabunganDao.delete(id);
			return ResponseEntity.ok().build();
		}
	}

}
