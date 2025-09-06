import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { addToCart } from "../store/slices/cartSlice";
import axios from "../axios";
import UpdateProduct from "./UpdateProduct";
const Product = () => {
  const { id } = useParams();
  const dispatch = useDispatch();
  const { user } = useSelector((state) => state.auth);
  const [product, setProduct] = useState(null);
  const [imageUrl, setImageUrl] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/product/${id}`
        );
        setProduct(response.data);
        // Always fetch image, backend will provide placeholder if needed
        fetchImage();
      } catch (error) {
        console.error("Error fetching product:", error);
      }
    };

    const fetchImage = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/product/${id}/image`,
          { responseType: "blob" }
        );
        setImageUrl(URL.createObjectURL(response.data));
      } catch (error) {
        console.error("Error fetching image:", error);
        setImageUrl(""); // Fallback for image loading error
      }
    };

    if (id) {
      fetchProduct();
    }
  }, [id]);

  const handlAddToCart = async () => {
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
  if (!product) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="pt-16 min-h-screen bg-gray-50 dark:bg-gray-900">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Product Image */}
          <div className="space-y-4">
            <div className="aspect-square bg-white dark:bg-gray-800 rounded-lg shadow-lg overflow-hidden">
              <img
                src={imageUrl}
                alt={product.imageName}
                className="w-full h-full object-cover"
              />
            </div>
          </div>

          {/* Product Details */}
          <div className="space-y-6">
            {/* Category and Date */}
            <div className="flex justify-between items-start">
              <span className="inline-block px-3 py-1 bg-blue-100 dark:bg-blue-900 text-blue-800 dark:text-blue-200 text-sm font-medium rounded-full">
                {product.category}
              </span>
              <div className="text-sm text-gray-600 dark:text-gray-400">
                <span className="font-medium">Listed:</span>{" "}
                <span className="italic">
                  {new Date(product.releaseDate).toLocaleDateString()}
                </span>
              </div>
            </div>

            {/* Product Name and Brand */}
            <div>
              <h1 className="text-3xl lg:text-4xl font-bold text-gray-900 dark:text-white mb-2">
                {product.name}
              </h1>
              <p className="text-lg text-gray-600 dark:text-gray-400 italic">
                {product.brand}
              </p>
            </div>

            {/* Description */}
            <div className="space-y-3">
              <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
                Product Description
              </h3>
              <p className="text-gray-700 dark:text-gray-300 leading-relaxed">
                {product.description}
              </p>
            </div>

            {/* Price and Actions */}
            <div className="space-y-4">
              <div className="flex items-center space-x-4">
                <span className="text-3xl font-bold text-gray-900 dark:text-white">
                  ₹{product.price}
                </span>
                <div className="flex items-center space-x-2">
                  <span className="text-sm text-gray-600 dark:text-gray-400">
                    Stock Available:
                  </span>
                  <span className="text-sm font-semibold text-green-600 dark:text-green-400">
                    {product.stockQuantity}
                  </span>
                </div>
              </div>

              <button
                className={`w-full py-3 px-6 rounded-lg font-medium text-lg transition-all duration-200 ${
                  product.productAvailable
                    ? "bg-blue-600 hover:bg-blue-700 text-white transform hover:scale-105 shadow-lg hover:shadow-xl"
                    : "bg-gray-400 text-gray-200 cursor-not-allowed"
                }`}
                onClick={handlAddToCart}
                disabled={!product.productAvailable}
              >
                {product.productAvailable ? "Add to Cart" : "Out of Stock"}
              </button>

              {!product.productAvailable && (
                <div className="flex items-center space-x-2 text-red-600 dark:text-red-400">
                  <svg
                    className="w-5 h-5"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"
                    />
                  </svg>
                  <span className="text-sm">
                    This product is currently out of stock
                  </span>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Product;
