defmodule RaindropsTest do
  use ExUnit.Case
  doctest Raindrops

  describe "Raindrops" do
    test "base cases" do
      assert Raindrops.raindrops(2) == "pling"
      assert Raindrops.raindrops(3) == "plang"
      assert Raindrops.raindrops(5) == "plong"
      assert Raindrops.raindrops(17) == "tshäng"
    end

    test "other cases" do
      assert Raindrops.raindrops(1) == "blob"
      assert Raindrops.raindrops(7) == "blob"
      assert Raindrops.raindrops(113) == "blob"
    end

    test "first transformation" do
      assert Raindrops.raindrops(4) == "PLING"
    end

    test "combining different transformations" do
      assert Raindrops.raindrops(6) == "*PLING*, PLANG"
      assert Raindrops.raindrops(10) == "*PLING* pling, PLONG"
      assert Raindrops.raindrops(34) == "*PLING* pling, TSHÄNG"
      assert Raindrops.raindrops(510) == "*PLING* pling, *PLANG* plang, *PLONG* plong, *TSHÄNG* tshäng"
    end
  end
end

"""
ExUnit.start()
"""
