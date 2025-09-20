package com.quickbite.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quickbite.entity.MenuItem;

/**
 * Repository interface for MenuItem entity operations.
 * 
 * This interface extends JpaRepository to provide CRUD operations
 * and includes custom query methods for specific business requirements
 * such as filtering by category, dietary tags, and price ranges.
 * 
 * @author QuickBite Development Team
 * @version 1.0.0
 */
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    /**
     * Find all active menu items
     * 
     * @return List of active menu items
     */
    List<MenuItem> findByIsActiveTrue();

    /**
     * Find all active menu items with pagination
     * 
     * @param pageable pagination information
     * @return Page of active menu items
     */
    Page<MenuItem> findByIsActiveTrue(Pageable pageable);

    /**
     * Find an active menu item by ID
     * 
     * @param id the menu item ID
     * @return Optional containing the menu item if found and active
     */
    Optional<MenuItem> findByIdAndIsActiveTrue(Long id);

    /**
     * Find menu items by category (case-insensitive)
     * 
     * @param category the category to search for
     * @return List of menu items in the specified category
     */
    @Query("SELECT m FROM MenuItem m WHERE LOWER(m.category) = LOWER(:category) AND m.isActive = true")
    List<MenuItem> findByCategoryIgnoreCase(@Param("category") String category);

    /**
     * Find menu items by category with pagination
     * 
     * @param category the category to search for
     * @param pageable pagination information
     * @return Page of menu items in the specified category
     */
    @Query("SELECT m FROM MenuItem m WHERE LOWER(m.category) = LOWER(:category) AND m.isActive = true")
    Page<MenuItem> findByCategoryIgnoreCase(@Param("category") String category, Pageable pageable);

    /**
     * Find menu items by dietary tag (case-insensitive)
     * 
     * @param dietaryTag the dietary tag to search for
     * @return List of menu items with the specified dietary tag
     */
    @Query("SELECT m FROM MenuItem m WHERE LOWER(m.dietaryTag) LIKE LOWER(CONCAT('%', :dietaryTag, '%')) AND m.isActive = true")
    List<MenuItem> findByDietaryTagContainingIgnoreCase(@Param("dietaryTag") String dietaryTag);

    /**
     * Find menu items by dietary tag with pagination
     * 
     * @param dietaryTag the dietary tag to search for
     * @param pageable   pagination information
     * @return Page of menu items with the specified dietary tag
     */
    @Query("SELECT m FROM MenuItem m WHERE LOWER(m.dietaryTag) LIKE LOWER(CONCAT('%', :dietaryTag, '%')) AND m.isActive = true")
    Page<MenuItem> findByDietaryTagContainingIgnoreCase(@Param("dietaryTag") String dietaryTag, Pageable pageable);

    /**
     * Find menu items within a price range
     * 
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return List of menu items within the price range
     */
    @Query("SELECT m FROM MenuItem m WHERE m.price >= :minPrice AND m.price <= :maxPrice AND m.isActive = true ORDER BY m.price ASC")
    List<MenuItem> findByPriceBetween(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Find menu items within a price range with pagination
     * 
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @param pageable pagination information
     * @return Page of menu items within the price range
     */
    @Query("SELECT m FROM MenuItem m WHERE m.price >= :minPrice AND m.price <= :maxPrice AND m.isActive = true")
    Page<MenuItem> findByPriceBetween(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    /**
     * Search menu items by name or description (case-insensitive)
     * 
     * @param searchTerm the term to search for
     * @return List of menu items matching the search term
     */
    @Query("SELECT m FROM MenuItem m WHERE (LOWER(m.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(m.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND m.isActive = true")
    List<MenuItem> searchByNameOrDescription(@Param("searchTerm") String searchTerm);

    /**
     * Search menu items by name or description with pagination
     * 
     * @param searchTerm the term to search for
     * @param pageable   pagination information
     * @return Page of menu items matching the search term
     */
    @Query("SELECT m FROM MenuItem m WHERE (LOWER(m.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(m.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND m.isActive = true")
    Page<MenuItem> searchByNameOrDescription(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Get all distinct categories from active menu items
     * 
     * @return List of distinct categories
     */
    @Query("SELECT DISTINCT m.category FROM MenuItem m WHERE m.isActive = true ORDER BY m.category")
    List<String> findDistinctCategories();

    /**
     * Get all distinct dietary tags from active menu items
     * 
     * @return List of distinct dietary tags
     */
    @Query("SELECT DISTINCT m.dietaryTag FROM MenuItem m WHERE m.dietaryTag IS NOT NULL AND m.isActive = true ORDER BY m.dietaryTag")
    List<String> findDistinctDietaryTags();

    /**
     * Check if a menu item name already exists (case-insensitive)
     * 
     * @param name the name to check
     * @return true if the name exists, false otherwise
     */
    @Query("SELECT COUNT(m) > 0 FROM MenuItem m WHERE LOWER(m.name) = LOWER(:name) AND m.isActive = true")
    boolean existsByNameIgnoreCase(@Param("name") String name);

    /**
     * Check if a menu item name already exists excluding a specific ID (for
     * updates)
     * 
     * @param name the name to check
     * @param id   the ID to exclude from the check
     * @return true if the name exists, false otherwise
     */
    @Query("SELECT COUNT(m) > 0 FROM MenuItem m WHERE LOWER(m.name) = LOWER(:name) AND m.id != :id AND m.isActive = true")
    boolean existsByNameIgnoreCaseAndIdNot(@Param("name") String name, @Param("id") Long id);
}