import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, useSearchParams } from "react-router-dom";
import { fetchProducts } from "../store/slices/productSlice";
import { addToCart } from "../store/slices/cartSlice";
import axios from "../axios";
import unplugged from "../assets/unplugged.png";

const Home = () => {
  const dispatch = useDispatch();
  const [searchParams] = useSearchParams();
  const { products, loading, error, pagination } = useSelector(
    (state) => state.product
  );
  const { user } = useSelector((state) => state.auth);
  const [selectedCategory, setSelectedCategory] = useState("");
  const [productsWithImages, setProductsWithImages] = useState([]);
  const [imagesLoading, setImagesLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [sortBy, setSortBy] = useState("id");
  const [sortDir, setSortDir] = useState("asc");
  const [categoryProducts, setCategoryProducts] = useState([]);
  const [categoryLoading, setCategoryLoading] = useState(false);

  // Handle category filtering from URL parameters
  useEffect(() => {
    const categoryFromUrl = searchParams.get("category");
    if (categoryFromUrl) {
      setSelectedCategory(categoryFromUrl);
      fetchCategoryProducts(categoryFromUrl);
    } else {
      setSelectedCategory("");
      setCategoryProducts([]);
      dispatch(fetchProducts({ page: currentPage, size: 12, sortBy, sortDir }));
    }
  }, [searchParams, dispatch, currentPage, sortBy, sortDir]);

  const fetchCategoryProducts = async (category) => {
    setCategoryLoading(true);
    try {
      const response = await axios.get(
        `/products/category/${encodeURIComponent(category)}`
      );
      setCategoryProducts(response.data);
      console.log(`Products for category ${category}:`, response.data);
    } catch (error) {
      console.error("Error fetching category products:", error);
      setCategoryProducts([]);
    } finally {
      setCategoryLoading(false);
    }
  };

  // Handle images for regular products
  useEffect(() => {
    if (products && products.length > 0 && !selectedCategory) {
      setImagesLoading(true);
      const fetchImagesAndUpdateProducts = async () => {
        const updatedProducts = await Promise.all(
          products.map(async (product) => {
            try {
              const response = await axios.get(`/product/${product.id}/image`, {
                responseType: "blob",
              });
              const imageUrl = URL.createObjectURL(response.data);
              return { ...product, imageUrl };
            } catch (error) {
              console.error(
                "Error fetching image for product ID:",
                product.id,
                error
              );
              // Create a placeholder image URL with product name
              const placeholderUrl = `data:image/svg+xml;base64,${btoa(`
                <svg width="200" height="150" xmlns="http://www.w3.org/2000/svg">
                  <defs>
                    <linearGradient id="grad" x1="0%" y1="0%" x2="100%" y2="100%">
                      <stop offset="0%" style="stop-color:#3B82F6;stop-opacity:1" />
                      <stop offset="100%" style="stop-color:#1D4ED8;stop-opacity:1" />
                    </linearGradient>
                  </defs>
                  <rect width="200" height="150" fill="url(#grad)"/>
                  <text x="100" y="80" font-family="Arial, sans-serif" font-size="16" font-weight="bold"
                        text-anchor="middle" fill="white">${product.name
                          .charAt(0)
                          .toUpperCase()}</text>
                </svg>
              `)}`;
              return { ...product, imageUrl: placeholderUrl };
            }
          })
        );
        setProductsWithImages(updatedProducts);
        setImagesLoading(false);
      };

      fetchImagesAndUpdateProducts();
    }
  }, [products, selectedCategory]);

  // Handle images for category products
  useEffect(() => {
    if (categoryProducts && categoryProducts.length > 0 && selectedCategory) {
      setImagesLoading(true);
      const fetchImagesAndUpdateProducts = async () => {
        const updatedProducts = await Promise.all(
          categoryProducts.map(async (product) => {
            try {
              const response = await axios.get(`/product/${product.id}/image`, {
                responseType: "blob",
              });
              const imageUrl = URL.createObjectURL(response.data);
              return { ...product, imageUrl };
            } catch (error) {
              console.error(
                "Error fetching image for product ID:",
                product.id,
                error
              );
              // Create a placeholder image URL with product name
              const placeholderUrl = `data:image/svg+xml;base64,${btoa(`
                <svg width="200" height="150" xmlns="http://www.w3.org/2000/svg">
                  <defs>
                    <linearGradient id="grad" x1="0%" y1="0%" x2="100%" y2="100%">
                      <stop offset="0%" style="stop-color:#3B82F6;stop-opacity:1" />
                      <stop offset="100%" style="stop-color:#1D4ED8;stop-opacity:1" />
                    </linearGradient>
                  </defs>
                  <rect width="200" height="150" fill="url(#grad)"/>
                  <text x="100" y="80" font-family="Arial, sans-serif" font-size="16" font-weight="bold"
                        text-anchor="middle" fill="white">${product.name
                          .charAt(0)
                          .toUpperCase()}</text>
                </svg>
              `)}`;
              return { ...product, imageUrl: placeholderUrl };
            }
          })
        );
        setProductsWithImages(updatedProducts);
        setImagesLoading(false);
      };

      fetchImagesAndUpdateProducts();
    }
  }, [categoryProducts, selectedCategory]);

  // Use category products when filtering by category, otherwise use regular products
  const displayProducts = selectedCategory
    ? productsWithImages
    : productsWithImages;

  const handleAddToCart = async (product) => {
    if (!user) {
      alert("Please login to add items to cart");
      return;
    }

    try {
      await dispatch(
        addToCart({ userId: user.id, productId: product.id, quantity: 1 })
      ).unwrap();
      alert("Product added to cart successfully!");
    } catch (error) {
      console.error("Failed to add product to cart:", error);
      alert("Failed to add product to cart. Please try again.");
    }
  };

  if (loading || imagesLoading || categoryLoading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="text-center">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-600 mx-auto mb-4"></div>
          <p className="text-gray-600 dark:text-gray-400">
            {loading
              ? "Loading products..."
              : categoryLoading
              ? "Loading category products..."
              : "Loading images..."}
          </p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="text-center">
          <img src={unplugged} alt="Error" className="w-24 h-24 mx-auto mb-4" />
          <h2 className="text-xl text-gray-600 dark:text-gray-400">
            Something went wrong
          </h2>
        </div>
      </div>
    );
  }

  return (
    <div className="pt-16 min-h-screen bg-gray-50 dark:bg-gray-900">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Sorting and Pagination Controls */}
        <div className="mb-6 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
          {/* Sorting Controls */}
          <div className="flex items-center gap-4">
            <label className="text-sm font-medium text-gray-700 dark:text-gray-300">
              Sort by:
            </label>
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
              className="px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md bg-white dark:bg-gray-700 text-gray-900 dark:text-white text-sm focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value="id">Default</option>
              <option value="name">Name</option>
              <option value="price">Price</option>
              <option value="category">Category</option>
            </select>
            <select
              value={sortDir}
              onChange={(e) => setSortDir(e.target.value)}
              className="px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md bg-white dark:bg-gray-700 text-gray-900 dark:text-white text-sm focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value="asc">Ascending</option>
              <option value="desc">Descending</option>
            </select>
          </div>

          {/* Pagination Info */}
          <div className="text-sm text-gray-600 dark:text-gray-400">
            Showing {pagination.currentPage * pagination.pageSize + 1} to{" "}
            {Math.min(
              (pagination.currentPage + 1) * pagination.pageSize,
              pagination.totalItems
            )}{" "}
            of {pagination.totalItems} products
          </div>
        </div>

        {/* Category Filter Display */}
        {selectedCategory && (
          <div className="mb-6 p-4 bg-blue-50 dark:bg-blue-900/20 rounded-lg border border-blue-200 dark:border-blue-800">
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-semibold text-blue-800 dark:text-blue-200">
                Products in "{selectedCategory}" category
              </h2>
              <Link
                to="/"
                className="text-sm text-blue-600 dark:text-blue-400 hover:text-blue-800 dark:hover:text-blue-200 underline"
              >
                Show all products
              </Link>
            </div>
          </div>
        )}

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {displayProducts.length === 0 ? (
            <div className="col-span-full flex justify-center items-center py-16">
              <div className="text-center">
                <svg
                  className="w-16 h-16 mx-auto mb-4 text-gray-400"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"
                  />
                </svg>
                <h2 className="text-xl font-semibold text-gray-600 dark:text-gray-400">
                  {selectedCategory
                    ? `No products found in "${selectedCategory}" category`
                    : "No Products Available"}
                </h2>
              </div>
            </div>
          ) : (
            displayProducts.map((product) => {
              const {
                id,
                brand = "Unknown Brand",
                name = "Unnamed Product",
                price = 0,
                productAvailable = false,
                imageUrl,
                stockQuantity = 0,
              } = product;

              return (
                <div
                  key={id}
                  className={`bg-white dark:bg-gray-800 rounded-lg shadow-md overflow-hidden transition-all duration-300 hover:shadow-lg hover:scale-105 flex flex-col ${
                    !productAvailable ? "opacity-60" : ""
                  }`}
                >
                  <Link to={`/product/${id}`} className="flex-1 flex flex-col">
                    {/* Product Image */}
                    <div className="relative w-full h-48 bg-gray-100 dark:bg-gray-700">
                      <img
                        src={imageUrl}
                        alt={name}
                        className="w-full h-full object-cover"
                        onError={(e) => {
                          // Fallback to a simple colored div if image fails
                          e.target.style.display = "none";
                          e.target.nextSibling.style.display = "flex";
                        }}
                      />
                      {/* Fallback div for when image fails to load */}
                      <div
                        className="w-full h-full bg-gradient-to-br from-blue-500 to-blue-700 flex items-center justify-center text-white text-4xl font-bold"
                        style={{ display: "none" }}
                      >
                        {name.charAt(0).toUpperCase()}
                      </div>
                    </div>

                    {/* Product Details */}
                    <div className="p-4 flex flex-col flex-1">
                      <div className="flex-grow">
                        <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-2 line-clamp-2 min-h-[3.5rem]">
                          {name.toUpperCase()}
                        </h3>
                        <p className="text-sm text-gray-600 dark:text-gray-400 italic mb-3">
                          ~ {brand}
                        </p>
                      </div>

                      <div className="border-t border-gray-200 dark:border-gray-700 pt-3 mt-auto">
                        <div className="flex items-center justify-between mb-3">
                          <span className="text-xl font-bold text-gray-900 dark:text-white">
                            ₹{price}
                          </span>
                          <span className="text-xs text-gray-500 dark:text-gray-400">
                            Stock: {product.stockQuantity}
                          </span>
                        </div>

                        <button
                          className={`w-full py-2 px-4 rounded-md font-medium transition-all duration-200 ${
                            productAvailable
                              ? "bg-blue-600 hover:bg-blue-700 text-white transform hover:scale-105"
                              : "bg-gray-400 text-gray-200 cursor-not-allowed"
                          }`}
                          onClick={(e) => {
                            e.preventDefault();
                            handleAddToCart(product);
                          }}
                          disabled={!productAvailable}
                        >
                          {productAvailable ? "Add to Cart" : "Out of Stock"}
                        </button>
                      </div>
                    </div>
                  </Link>
                </div>
              );
            })
          )}
        </div>

        {/* Pagination Controls - Only show when not filtering by category */}
        {!selectedCategory && pagination.totalPages > 1 && (
          <div className="mt-8 flex justify-center">
            <nav className="flex items-center space-x-2">
              {/* Previous Button */}
              <button
                onClick={() => setCurrentPage(Math.max(0, currentPage - 1))}
                disabled={!pagination.hasPrevious}
                className={`px-3 py-2 rounded-md text-sm font-medium ${
                  pagination.hasPrevious
                    ? "bg-white dark:bg-gray-800 text-gray-700 dark:text-gray-300 border border-gray-300 dark:border-gray-600 hover:bg-gray-50 dark:hover:bg-gray-700"
                    : "bg-gray-100 dark:bg-gray-700 text-gray-400 dark:text-gray-500 cursor-not-allowed"
                }`}
              >
                Previous
              </button>

              {/* Page Numbers */}
              {Array.from(
                { length: Math.min(5, pagination.totalPages) },
                (_, i) => {
                  let pageNum;
                  if (pagination.totalPages <= 5) {
                    pageNum = i;
                  } else if (currentPage < 3) {
                    pageNum = i;
                  } else if (currentPage >= pagination.totalPages - 3) {
                    pageNum = pagination.totalPages - 5 + i;
                  } else {
                    pageNum = currentPage - 2 + i;
                  }

                  return (
                    <button
                      key={pageNum}
                      onClick={() => setCurrentPage(pageNum)}
                      className={`px-3 py-2 rounded-md text-sm font-medium ${
                        currentPage === pageNum
                          ? "bg-blue-600 text-white"
                          : "bg-white dark:bg-gray-800 text-gray-700 dark:text-gray-300 border border-gray-300 dark:border-gray-600 hover:bg-gray-50 dark:hover:bg-gray-700"
                      }`}
                    >
                      {pageNum + 1}
                    </button>
                  );
                }
              )}

              {/* Next Button */}
              <button
                onClick={() =>
                  setCurrentPage(
                    Math.min(pagination.totalPages - 1, currentPage + 1)
                  )
                }
                disabled={!pagination.hasNext}
                className={`px-3 py-2 rounded-md text-sm font-medium ${
                  pagination.hasNext
                    ? "bg-white dark:bg-gray-800 text-gray-700 dark:text-gray-300 border border-gray-300 dark:border-gray-600 hover:bg-gray-50 dark:hover:bg-gray-700"
                    : "bg-gray-100 dark:bg-gray-700 text-gray-400 dark:text-gray-500 cursor-not-allowed"
                }`}
              >
                Next
              </button>
            </nav>
          </div>
        )}
      </div>
    </div>
  );
};

export default Home;
