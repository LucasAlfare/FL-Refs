package com.lucasalfare.flrefs.main

import com.lucasalfare.flrefs.main.model.ItemResponseDTO

/**
 * Repository interface defining operations for interacting with items in the application's database.
 */
interface AppRepository {

  /**
   * Creates an item with the specified attributes.
   *
   * @param title The title of the item.
   * @param description The description of the item.
   * @param category The category of the item.
   * @param name The name associated with the item.
   * @param originalUrl The original URL associated with the item.
   * @param thumbnailUrl The thumbnail URL associated with the item.
   * @param concatenation Additional concatenation data for the item.
   * @return An array of strings representing the created item.
   */
  suspend fun create(
    title: String,
    description: String,
    category: String,
    name: String,
    originalUrl: String,
    thumbnailUrl: String,
    concatenation: String
  ): Array<String>

  /**
   * Retrieves a list of items based on the search term, with optional limits and offsets.
   *
   * @param term The search term to filter items.
   * @param maxItems The maximum number of items to retrieve.
   * @param offset The offset for pagination.
   * @return A list of [ItemResponseDTO] representing the retrieved items.
   */
  suspend fun getAll(
    term: String = "",
    maxItems: Int = 0,
    offset: Int = 0
  ): List<ItemResponseDTO>
}

/**
 * Abstract class implementing [AppRepository] interface with default unimplemented methods.
 * Typically used as a base for repository adapters.
 */
abstract class AppRepositoryAdapter : AppRepository {

  override suspend fun create(
    title: String,
    description: String,
    category: String,
    name: String,
    originalUrl: String,
    thumbnailUrl: String,
    concatenation: String
  ): Array<String> {
    throw UnavailableDatabaseService("Not implemented")
  }

  override suspend fun getAll(
    term: String,
    maxItems: Int,
    offset: Int
  ): List<ItemResponseDTO> {
    throw UnavailableDatabaseService("Not implemented")
  }
}