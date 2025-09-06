import React, { useEffect, useMemo, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import axios from "../axios";
import {
  fetchUserCart,
  updateCartQuantity,
  removeFromCart as removeFromCartThunk,
  clearCart as clearCartThunk,
} from "../store/slices/cartSlice";
import Checkout from "./Checkout";

const Cart = () => {
  const dispatch = useDispatch();
  const { user } = useSelector((state) => state.auth);
  const { items, loading } = useSelector((state) => state.cart);
  const [imageUrlByProductId, setImageUrlByProductId] = useState({});
  const [isCheckoutOpen, setIsCheckoutOpen] = useState(false);

  useEffect(() => {
    if (user?.id) {
      dispatch(fetchUserCart(user.id));
    }
  }, [dispatch, user]);

  useEffect(() => {
    const loadImages = async () => {
      const productIds = items.map((it) => it.product.id);
      const uniqueIds = Array.from(new Set(productIds));
      const entries = await Promise.all(
        uniqueIds.map(async (pid) => {
          try {
            const res = await axios.get(`/product/${pid}/image`, {
              responseType: "blob",
            });
            return [pid, URL.createObjectURL(res.data)];
          } catch {
            return [pid, ""];
          }
        })
      );
      setImageUrlByProductId(Object.fromEntries(entries));
    };
    if (items.length > 0) {
      loadImages();
    }
  }, [items]);

  const totalPrice = useMemo(
    () => items.reduce((sum, it) => sum + Number(it.price) * it.quantity, 0),
    [items]
  );

  const handleIncreaseQuantity = (item) => {
    const next = item.quantity + 1;
    if (next > item.product.stockQuantity) {
      alert("Cannot add more than available stock");
      return;
    }
    dispatch(
      updateCartQuantity({
        userId: user.id,
        productId: item.product.id,
        quantity: next,
      })
    );
  };

  const handleDecreaseQuantity = (item) => {
    const next = Math.max(item.quantity - 1, 1);
    if (next !== item.quantity) {
      dispatch(
        updateCartQuantity({
          userId: user.id,
          productId: item.product.id,
          quantity: next,
        })
      );
    }
  };

  const handleRemoveFromCart = (item) => {
    dispatch(
      removeFromCartThunk({ userId: user.id, productId: item.product.id })
    );
  };

  const handleClearCart = () => {
    dispatch(clearCartThunk(user.id));
  };

  return (
    <div className="pt-16 min-h-screen bg-gray-50 dark:bg-gray-900">
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="bg-white dark:bg-gray-800 shadow-lg rounded-lg overflow-hidden">
          <div className="px-6 py-4 border-b border-gray-200 dark:border-gray-700">
            <h1 className="text-2xl font-bold text-gray-900 dark:text-white">
              Shopping Cart
            </h1>
          </div>

          {loading ? (
            <div className="flex justify-center items-center py-16">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            </div>
          ) : items.length === 0 ? (
            <div className="text-center py-16">
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
                  d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-2.5 5M7 13l2.5 5m6-5v6a2 2 0 01-2 2H9a2 2 0 01-2-2v-6m8 0V9a2 2 0 00-2-2H9a2 2 0 00-2 2v4.01"
                />
              </svg>
              <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-2">
                Your cart is empty
              </h3>
              <p className="text-gray-600 dark:text-gray-400">
                Start shopping to add items to your cart
              </p>
            </div>
          ) : (
            <div className="divide-y divide-gray-200 dark:divide-gray-700">
              {items.map((item) => (
                <div key={item.id} className="p-6">
                  <div className="flex items-center space-x-4">
                    {/* Product Image */}
                    <div className="flex-shrink-0">
                      <img
                        src={imageUrlByProductId[item.product.id]}
                        alt={item.product.name}
                        className="w-20 h-20 object-cover rounded-lg"
                      />
                    </div>

                    {/* Product Details */}
                    <div className="flex-1 min-w-0">
                      <h3 className="text-lg font-medium text-gray-900 dark:text-white truncate">
                        {item.product.name}
                      </h3>
                      <p className="text-sm text-gray-600 dark:text-gray-400">
                        {item.product.brand}
                      </p>
                      <p className="text-sm text-gray-600 dark:text-gray-400">
                        ₹{item.price} each
                      </p>
                    </div>

                    {/* Quantity Controls */}
                    <div className="flex items-center space-x-2">
                      <button
                        onClick={() => handleDecreaseQuantity(item)}
                        className="p-1 rounded-md text-gray-400 hover:text-gray-600 dark:hover:text-gray-300"
                      >
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
                            d="M20 12H4"
                          />
                        </svg>
                      </button>
                      <span className="w-8 text-center text-gray-900 dark:text-white font-medium">
                        {item.quantity}
                      </span>
                      <button
                        onClick={() => handleIncreaseQuantity(item)}
                        className="p-1 rounded-md text-gray-400 hover:text-gray-600 dark:hover:text-gray-300"
                      >
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
                            d="M12 6v6m0 0v6m0-6h6m-6 0H6"
                          />
                        </svg>
                      </button>
                    </div>

                    {/* Total Price */}
                    <div className="text-right">
                      <p className="text-lg font-semibold text-gray-900 dark:text-white">
                        ₹{Number(item.price) * item.quantity}
                      </p>
                    </div>

                    {/* Remove Button */}
                    <button
                      onClick={() => handleRemoveFromCart(item)}
                      className="p-2 text-red-400 hover:text-red-600 dark:hover:text-red-300 transition-colors"
                    >
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
                          d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                        />
                      </svg>
                    </button>
                  </div>
                </div>
              ))}

              {/* Cart Summary */}
              <div className="p-6 bg-gray-50 dark:bg-gray-700">
                <div className="flex justify-between items-center mb-4">
                  <span className="text-lg font-semibold text-gray-900 dark:text-white">
                    Total:
                  </span>
                  <span className="text-2xl font-bold text-gray-900 dark:text-white">
                    ₹{totalPrice}
                  </span>
                </div>

                <div className="flex space-x-4">
                  <button
                    onClick={handleClearCart}
                    className="flex-1 py-2 px-4 border border-gray-300 dark:border-gray-600 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-600 transition-colors"
                  >
                    Clear Cart
                  </button>
                  <button
                    onClick={() => setIsCheckoutOpen(true)}
                    className="flex-1 py-2 px-4 bg-blue-600 hover:bg-blue-700 text-white rounded-md transition-colors"
                  >
                    Proceed to Checkout
                  </button>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Checkout Modal */}
      <Checkout
        isOpen={isCheckoutOpen}
        onClose={() => setIsCheckoutOpen(false)}
        cartItems={items}
        totalPrice={totalPrice}
      />
    </div>
  );
};

export default Cart;
