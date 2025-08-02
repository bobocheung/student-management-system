package com.student.service.impl;

import com.student.entity.Class;
import com.student.repository.ClassRepository;
import com.student.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClassServiceImpl implements ClassService {
    
    @Autowired
    private ClassRepository classRepository;
    
    @Override
    public Class saveClass(Class classEntity) {
        return classRepository.save(classEntity);
    }
    
    @Override
    public Optional<Class> findById(Long id) {
        return classRepository.findById(id);
    }
    
    @Override
    public Optional<Class> findByCode(String code) {
        return classRepository.findByCode(code);
    }
    
    @Override
    public List<Class> findAllClasses() {
        return classRepository.findAll();
    }
    
    @Override
    public Page<Class> findAllClasses(Pageable pageable) {
        return classRepository.findAll(pageable);
    }
    
    @Override
    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByCode(String code) {
        return classRepository.findByCode(code).isPresent();
    }
    
    @Override
    public List<Class> findByGrade(Integer grade) {
        return classRepository.findByGrade(grade);
    }
    
    @Override
    public List<Class> findByStatus(Class.ClassStatus status) {
        return classRepository.findByStatus(status);
    }
    
    @Override
    public List<Class> findByTeacherId(Long teacherId) {
        return classRepository.findByTeacherId(teacherId);
    }
    
    @Override
    public List<Class> findByKeyword(String keyword) {
        return classRepository.findByKeyword(keyword);
    }
    
    @Override
    public Page<Class> findByNameContaining(String name, Pageable pageable) {
        return classRepository.findByNameContaining(name, pageable);
    }
    
    @Override
    public Page<Class> findByCodeContaining(String code, Pageable pageable) {
        return classRepository.findByCodeContaining(code, pageable);
    }
    
    @Override
    public Page<Class> findByGrade(Integer grade, Pageable pageable) {
        return classRepository.findByGrade(grade, pageable);
    }
    
    @Override
    public Page<Class> findByStatus(Class.ClassStatus status, Pageable pageable) {
        return classRepository.findByStatus(status, pageable);
    }
    
    @Override
    public Page<Class> findByTeacherId(Long teacherId, Pageable pageable) {
        return classRepository.findByTeacherId(teacherId, pageable);
    }
    
    @Override
    public Page<Class> findByAdvancedSearch(String name, String code, Integer grade, 
                                           Class.ClassStatus status, Pageable pageable) {
        if (name != null && !name.trim().isEmpty()) {
            return classRepository.findByNameContaining(name, pageable);
        } else if (code != null && !code.trim().isEmpty()) {
            return classRepository.findByCodeContaining(code, pageable);
        } else if (grade != null) {
            return classRepository.findByGrade(grade, pageable);
        } else if (status != null) {
            return classRepository.findByStatus(status, pageable);
        } else {
            return classRepository.findAll(pageable);
        }
    }
    
    @Override
    public Long countByStatus(Class.ClassStatus status) {
        return classRepository.countByStatus(status);
    }
    
    @Override
    public Long countByGrade(Integer grade) {
        return classRepository.countByGrade(grade);
    }
    
    @Override
    public Long countByTeacherId(Long teacherId) {
        return classRepository.countByTeacherId(teacherId);
    }
    
    @Override
    public Class activateClass(Long classId) {
        Optional<Class> classOpt = classRepository.findById(classId);
        if (classOpt.isPresent()) {
            Class classEntity = classOpt.get();
            classEntity.setStatus(Class.ClassStatus.ACTIVE);
            return classRepository.save(classEntity);
        }
        throw new RuntimeException("班級不存在");
    }
    
    @Override
    public Class deactivateClass(Long classId) {
        Optional<Class> classOpt = classRepository.findById(classId);
        if (classOpt.isPresent()) {
            Class classEntity = classOpt.get();
            classEntity.setStatus(Class.ClassStatus.INACTIVE);
            return classRepository.save(classEntity);
        }
        throw new RuntimeException("班級不存在");
    }
    
    @Override
    public boolean validateClassCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        return code.matches("^[A-Z0-9]{2,10}$");
    }
    
    @Override
    public boolean validateClassName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.length() >= 2 && name.length() <= 50;
    }
} 